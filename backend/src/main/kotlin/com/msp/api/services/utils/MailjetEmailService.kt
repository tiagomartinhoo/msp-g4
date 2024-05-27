package com.msp.api.services.utils

import com.mailjet.client.ClientOptions
import com.mailjet.client.MailjetClient
import com.mailjet.client.transactional.SendContact
import com.mailjet.client.transactional.SendEmailsRequest
import com.mailjet.client.transactional.TrackOpens
import com.mailjet.client.transactional.TransactionalEmail
import com.mailjet.client.transactional.response.SendEmailsResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class MailjetEmailService(
    @Value("\${mailjet.api.key}") private val apiKey: String,
    @Value("\${mailjet.api.secret}") private val apiSecret: String
) {

    private val senderEmail = "tiagoalexmonteiro@hotmail.com"

    private val client: MailjetClient = MailjetClient(
        ClientOptions.builder()
            .apiKey(apiKey)
            .apiSecretKey(apiSecret)
            .build()
    )

    fun sendEmail(to: String, subject: String, text: String) {
        val message = TransactionalEmail
            .builder()
            .to(SendContact(to, to))
            .from(SendContact(senderEmail, senderEmail))
            .htmlPart(text)
            .subject(subject)
            .trackOpens(TrackOpens.ENABLED)
            .header("test-header-key", "test-value")
            .customID("custom-id-value")
            .build()

        val request = SendEmailsRequest
            .builder()
            .message(message)
            .build()

        // act
        val response: SendEmailsResponse = request.sendWith(client)
    }

    fun sendEmailWithDoctorsCredentials(name: String, email: String, password: String) {
        sendEmail(
            to = email,
            subject = "MyClinic App Credentials",
            text =
            """
            <h1>Welcome to MyClinic, Dr. $name!</h1>
            <p>We are excited to have you as part of our team.</p>
            <br/>
            <p>Here are your login credentials:</p>
            <p><strong>Email:</strong> $email</p>
            <p><strong>Password:</strong> $password</p>
            <br/>
            <p>If you have any questions or need assistance, please do not hesitate to contact our support team.</p>
            <br/>
            <p>Best regards,<br/>The MyClinic Team</p>
            """.trimIndent()
        )
    }
}
