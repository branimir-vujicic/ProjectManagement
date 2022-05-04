package rs.ac.su.vts.pm.projectmanagement.model.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Mail
{

    private String mailFrom;

    private String mailTo;

    private String mailCc;

    private String mailBcc;

    private String mailSubject;

    private String mailContent;

    private String contentType;

    private List<Object> attachments;

    public Mail()
    {
        contentType = "text/plain";
    }
}
