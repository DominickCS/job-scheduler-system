package com.dominickcs.job_scheduler_system.executor;

import java.util.Date;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.dominickcs.job_scheduler_system.jobparameters.EmailParameters;
import com.dominickcs.job_scheduler_system.model.Job;
import com.dominickcs.job_scheduler_system.model.JobType;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class EmailNotificationExecutor implements JobExecutor {

  private JavaMailSender mailSender;
  private ObjectMapper objectMapper;

  public EmailNotificationExecutor(JavaMailSender mailSender, ObjectMapper objectMapper) {
    this.mailSender = mailSender;
    this.objectMapper = objectMapper;
  }

  @Override
  public JobExecutorResult execute(Job job) {
    // Capture current time in ms for execution time calculation
    long startTime = System.currentTimeMillis();
    try {
      EmailParameters emailParameters = objectMapper.readValue(job.getJobParameters(), EmailParameters.class);

      // Configure a MimeMessage and set parameters from job parameter values
      MimeMessage message = mailSender.createMimeMessage();
      message.setFrom(emailParameters.getEmailFrom());
      message.addRecipients(Message.RecipientType.TO, emailParameters.getEmailTo());
      message.setSubject(emailParameters.getEmailSubject());
      message.setSentDate(new Date());
      message.setText(emailParameters.getEmailBody());

      // Send the email message
      mailSender.send(message);

      // Calculate the execution time in ms
      long executionTime = System.currentTimeMillis() - startTime;

      // Return results
      return new JobExecutorResult(true, "Email was sent successfully!", null, executionTime);

    } catch (MessagingException messagingException) {
      return new JobExecutorResult(false, null, "A message exception occurred: " + messagingException, 0L);
    } catch (org.springframework.mail.MailException mailException) {
      return new JobExecutorResult(false, null, "Mail send failed: " + mailException.getMessage(), 0L);
    } catch (JsonProcessingException jsonProcessingException) {
      return new JobExecutorResult(false, null, "Failed to process email parameters: " + jsonProcessingException, 0L);
    }
  }

  @Override
  public boolean supports(JobType jobType) {
    if (jobType == JobType.EMAIL_NOTIFICATION) {
      return true;
    } else {
      return false;
    }
  }

}
