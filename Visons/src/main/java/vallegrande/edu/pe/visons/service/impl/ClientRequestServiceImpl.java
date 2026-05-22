package vallegrande.edu.pe.visons.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import vallegrande.edu.pe.visons.dto.ClientRequestActionRequest;
import vallegrande.edu.pe.visons.dto.ClientRequestActionResponse;
import vallegrande.edu.pe.visons.model.Client;
import vallegrande.edu.pe.visons.model.ClientRequest;
import vallegrande.edu.pe.visons.model.Role;
import vallegrande.edu.pe.visons.model.UserAccount;
import vallegrande.edu.pe.visons.model.UserType;
import vallegrande.edu.pe.visons.repository.ClientRepository;
import vallegrande.edu.pe.visons.repository.ClientRequestRepository;
import vallegrande.edu.pe.visons.repository.RoleRepository;
import vallegrande.edu.pe.visons.repository.UserAccountRepository;
import vallegrande.edu.pe.visons.repository.UserRoleRepository;
import vallegrande.edu.pe.visons.repository.UserTypeRepository;
import vallegrande.edu.pe.visons.service.ClientRequestService;

@Slf4j
@Service
public class ClientRequestServiceImpl implements ClientRequestService {

    private static final String STATUS_PENDING = "Pending";
    private static final String STATUS_APPROVED = "Approved";
    private static final String STATUS_REJECTED = "Rejected";
    private static final String USER_TYPE_CLIENT = "CLIENT";
    private static final String ROLE_CLIENT = "CLIENT";

    private final ClientRequestRepository clientRequestRepository;
    private final ClientRepository clientRepository;
    private final UserAccountRepository userAccountRepository;
    private final UserTypeRepository userTypeRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final JavaMailSender mailSender;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final String clientLoginUrl;
    private final String mailFrom;

    public ClientRequestServiceImpl(ClientRequestRepository clientRequestRepository,
            ClientRepository clientRepository,
            UserAccountRepository userAccountRepository,
            UserTypeRepository userTypeRepository,
            RoleRepository roleRepository,
            UserRoleRepository userRoleRepository,
            ObjectProvider<JavaMailSender> mailSenderProvider,
            Environment environment) {
        this.clientRequestRepository = clientRequestRepository;
        this.clientRepository = clientRepository;
        this.userAccountRepository = userAccountRepository;
        this.userTypeRepository = userTypeRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.mailSender = mailSenderProvider.getIfAvailable();
        this.clientLoginUrl = environment.getProperty("app.client-login-url", "http://localhost:4200/login");
        this.mailFrom = environment.getProperty("app.mail.from", "no-reply@visons.local");
    }

    @Override
    public List<ClientRequest> findAll() {
        return clientRequestRepository.findAll(Sort.by(Sort.Direction.DESC, "requestDate"));
    }

    @Override
    public List<ClientRequest> findByStatus(String status) {
        return clientRequestRepository.findByStatusIgnoreCase(normalizeStatus(status));
    }

    @Override
    public Optional<ClientRequest> findById(Integer id) {
        return clientRequestRepository.findById(id);
    }

    @Override
    @Transactional
    public ClientRequest save(ClientRequest clientRequest) {
        ensureUniqueClientRequest(clientRequest);
        clientRequest.setCountry(requireText(clientRequest.getCountry(), "country"));
        clientRequest.setRequestId(null);
        clientRequest.setStatus(STATUS_PENDING);
        clientRequest.setRequestDate(LocalDateTime.now());
        clientRequest.setReviewedBy(null);
        clientRequest.setComments(normalizeNullable(clientRequest.getComments()));
        return clientRequestRepository.save(clientRequest);
    }

    @Override
    @Transactional
    public ClientRequestActionResponse approveRequest(Integer id, ClientRequestActionRequest request) {
        ClientRequest clientRequest = getPendingRequest(id);
        String companyName = requireText(clientRequest.getCompanyName(), "companyName");
        String taxId = requireText(clientRequest.getTaxId(), "taxId");
        String country = normalizeNullable(clientRequest.getCountry());
        if (country == null) {
            country = "Peru";
            clientRequest.setCountry(country);
        }
        String email = requireText(clientRequest.getEmail(), "email");
        String phone = normalizeNullable(clientRequest.getPhone());
        String address = normalizeNullable(clientRequest.getAddress());

        validateApprovalUniqueness(taxId, email);

        Client client = new Client();
        client.setCompanyName(companyName);
        client.setTaxId(taxId);
        client.setCountry(country);
        client.setPhone(phone);
        client.setAddress(address);
        client.setEmail(email);
        client.setActive(Boolean.TRUE);
        client.setCreatedAt(LocalDateTime.now());
        client.setUpdatedAt(LocalDateTime.now());
        Client savedClient = clientRepository.save(client);

        UserType userType = getOrCreateUserType(USER_TYPE_CLIENT);
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername(email);
        userAccount.setPasswordHash(passwordEncoder.encode(taxId));
        userAccount.setUserTypeId(userType.getId());
        userAccount.setClientId(savedClient.getClientId());
        userAccount.setWorkerId(null);
        userAccount.setActive(Boolean.TRUE);
        userAccount.setCreatedAt(LocalDateTime.now());
        UserAccount savedUser = userAccountRepository.save(userAccount);

        Role clientRole = getOrCreateRole(ROLE_CLIENT);
        userRoleRepository.assignRole(savedUser.getUserId(), clientRole.getRoleId());

        clientRequest.setStatus(STATUS_APPROVED);
        clientRequest.setReviewedBy(resolveReviewer(request));
        clientRequestRepository.save(clientRequest);

        sendApprovalEmail(email, companyName, email, taxId);

        return buildActionResponse(clientRequest, savedClient.getClientId(), savedUser.getUserId(),
                "Solicitud aprobada correctamente");
    }

    @Override
    @Transactional
    public ClientRequestActionResponse rejectRequest(Integer id, ClientRequestActionRequest request) {
        ClientRequest clientRequest = getPendingRequest(id);
        clientRequest.setStatus(STATUS_REJECTED);
        clientRequest.setReviewedBy(resolveReviewer(request));
        if (request != null && request.getComments() != null && !request.getComments().isBlank()) {
            clientRequest.setComments(request.getComments().trim());
        }
        clientRequestRepository.save(clientRequest);

        sendRejectionEmail(requireText(clientRequest.getEmail(), "email"),
                requireText(clientRequest.getFirstName(), "firstName"),
                requireText(clientRequest.getLastName(), "lastName"));

        return buildActionResponse(clientRequest, null, null, "Solicitud rechazada correctamente");
    }

    private void ensureUniqueClientRequest(ClientRequest clientRequest) {
        if (clientRequest == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Client request inválido");
        }

        if (clientRequest.getUsername() != null && !clientRequest.getUsername().isBlank()) {
            clientRequestRepository.findByUsernameIgnoreCase(clientRequest.getUsername().trim())
                    .ifPresent(existing -> { throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "El username ya fue registrado en una solicitud previa"); });
        }

        if (clientRequest.getEmail() != null && !clientRequest.getEmail().isBlank()) {
            clientRequestRepository.findByEmailIgnoreCase(clientRequest.getEmail().trim())
                    .ifPresent(existing -> { throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "El correo ya fue registrado en una solicitud previa"); });
        }
    }

    private ClientRequest getPendingRequest(Integer id) {
        ClientRequest clientRequest = clientRequestRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitud no encontrada"));

        if (!STATUS_PENDING.equalsIgnoreCase(normalizeStatus(clientRequest.getStatus()))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La solicitud ya fue procesada");
        }

        return clientRequest;
    }

    private void validateApprovalUniqueness(String taxId, String email) {
        clientRepository.findByTaxIdIgnoreCase(taxId)
                .ifPresent(existing -> { throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Ya existe un cliente registrado con este tax ID"); });

        clientRepository.findByEmailIgnoreCase(email)
                .ifPresent(existing -> { throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Ya existe un cliente registrado con este correo"); });

        userAccountRepository.findByUsernameIgnoreCase(email)
                .ifPresent(existing -> { throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Ya existe un usuario registrado con este correo"); });
    }

    private UserType getOrCreateUserType(String name) {
        return userTypeRepository.findByName(name)
                .orElseGet(() -> {
                    UserType userType = new UserType();
                    userType.setName(name);
                    return userTypeRepository.save(userType);
                });
    }

    private Role getOrCreateRole(String name) {
        return roleRepository.findByName(name)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(name);
                    role.setDescription("Rol de cliente");
                    return roleRepository.save(role);
                });
    }

    private ClientRequestActionResponse buildActionResponse(ClientRequest clientRequest, Integer clientId,
            Integer userId, String message) {
        ClientRequestActionResponse response = new ClientRequestActionResponse();
        response.setMessage(message);
        response.setRequestId(clientRequest.getRequestId());
        response.setStatus(clientRequest.getStatus());
        response.setClientId(clientId);
        response.setUserId(userId);
        response.setLoginUsername(clientRequest.getEmail());
        return response;
    }

    private void sendApprovalEmail(String email, String companyName, String loginUsername, String temporaryPassword) {
        String subject = "Solicitud de acceso aprobada";
        String html = """
                <div style="font-family: Arial, sans-serif; color: #1f2937; line-height: 1.6;">
                  <p>Hola %s,</p>
                  <p>Su solicitud fue aprobada exitosamente. Ya puede acceder a la plataforma.</p>
                  <p><strong>Usuario:</strong> %s<br>
                  <strong>Contraseña temporal:</strong> %s</p>
                  <p style="margin: 24px 0;">
                    <a href="%s" style="display:inline-block;background:#2563eb;color:#ffffff;text-decoration:none;padding:12px 18px;border-radius:8px;">Iniciar sesión</a>
                  </p>
                  <p>Si necesita ayuda, puede responder este correo.</p>
                  <p>Saludos cordiales,<br>Equipo VISONS</p>
                </div>
                """.formatted(companyName, loginUsername, temporaryPassword, clientLoginUrl);
        sendHtmlEmail(email, subject, html);
    }

    private void sendRejectionEmail(String email, String firstName, String lastName) {
        String subject = "Resultado de su solicitud de acceso";
        String fullName = (firstName + " " + lastName).trim();
        String html = """
                <div style="font-family: Arial, sans-serif; color: #1f2937; line-height: 1.6;">
                  <p>Hola %s,</p>
                  <p>Lamentamos informarle que su solicitud no fue aprobada en esta oportunidad.</p>
                  <p>Actualmente no cumple con los requisitos necesarios, pero puede volver a intentarlo más adelante.</p>
                  <p>Gracias por su interés en la plataforma.</p>
                  <p>Saludos cordiales,<br>Equipo VISONS</p>
                </div>
                """.formatted(fullName);
        sendHtmlEmail(email, subject, html);
    }

    private void sendHtmlEmail(String to, String subject, String htmlBody) {
        if (mailSender == null) {
            log.warn("Mail sender no configurado; mostrando correo en logs (modo desarrollo) - destinatario={}", to);
            log.info("--- Email preview START ---\nTo: {}\nSubject: {}\nBody:\n{}\n--- Email preview END ---", to, subject, htmlBody);
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(mailFrom);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(message);
        } catch (Exception ex) {
            log.warn("No se pudo enviar correo a {}: {}", to, ex.getMessage());
        }
    }

    private Integer resolveReviewer(ClientRequestActionRequest request) {
        return request == null ? null : request.getReviewedBy();
    }

    private String normalizeStatus(String status) {
        return status == null ? STATUS_PENDING : status.trim();
    }

    private String normalizeNullable(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isBlank() ? null : trimmed;
    }

    private String requireText(String value, String fieldName) {
        String normalized = normalizeNullable(value);
        if (normalized == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, fieldName + " es requerido");
        }
        return normalized;
    }
}
