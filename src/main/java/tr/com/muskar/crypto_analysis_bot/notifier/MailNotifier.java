package tr.com.muskar.crypto_analysis_bot.notifier;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailNotifier {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${alert.mail.to}")
    private String to;



    public void sendSignal(String coin, String signal, double price) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(" Kripto Sinyali: " + coin.toUpperCase());
        message.setText("Sinyal: " + signal + "\nFiyat: $" + price + "\nZaman: " + java.time.LocalDateTime.now());

        try {
            mailSender.send(message);
            System.out.println(" Mail gönderildi: " + signal);
        } catch (Exception e) {
            System.err.println(" Mail gönderilemedi: " + e.getMessage());
        }
    }
}

