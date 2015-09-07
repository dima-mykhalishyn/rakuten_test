package org.test.project.support;
import org.apache.commons.lang.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Date Formatter Tag
 * @author dmikhalishin@provectus-it.com
 * @see TagSupport
 */
public class DateTag extends TagSupport {

    private String format;
    private Date dateValue;

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Date getDateValue() {
        return dateValue;
    }

    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    /**
     * {@inheritDoc}
     * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
     */
    @Override
    public int doStartTag() throws JspException {
        try {
            if(StringUtils.isNotBlank(format) && dateValue != null){
                JspWriter out = pageContext.getOut();
                final SimpleDateFormat frm = new SimpleDateFormat(format);
                out.print(frm.format(dateValue));
            }
        } catch (IOException e) {
            throw new JspException(e);
        }
        return SKIP_BODY;
    }
}
