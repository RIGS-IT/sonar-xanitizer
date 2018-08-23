package org.owasp.webgoat.session;

import java.io.PrintWriter;
import java.util.Properties;
import org.apache.ecs.Element;
import org.apache.ecs.HtmlColor;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.A;
import org.apache.ecs.html.Font;
import org.apache.ecs.html.IMG;
import org.owasp.webgoat.lessons.AbstractLesson;

/**
 * *************************************************************************************************
 *
 *
 * This file is part of WebGoat, an Open Web Application Security Project
 * utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 20014 Bruce Mayhew
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * Getting Source ==============
 *
 * Source for this application is maintained at
 * https://github.com/WebGoat/WebGoat, a repository for free software projects.
 *
 * @author Jeff Williams <a href="http://www.aspectsecurity.com">Aspect
 * Security</a>
 * @since October 28, 2003
 * @version $Id: $Id
 */
public abstract class Screen {

    /**
     * Description of the Field
     */
    public static int MAIN_SIZE = 375;

    // private Head head;
    private Element content;

    final static IMG logo = new IMG("images/aspectlogo-horizontal-small.jpg").setAlt("Aspect Security").setBorder(0)
            .setHspace(0).setVspace(0);

    /**
     * Constructor for the Screen object
     */
    public Screen() {
    }

    // FIXME: Each lesson should have a role assigned to it. Each user/student
    // should also have a role(s) assigned. The user would only be allowed
    // to see lessons that correspond to their role. Eventually these roles
    // will be stored in the internal database. The user will be able to hack
    // into the database and change their role. This will allow the user to
    // see the admin screens, once they figure out how to turn the admin switch on.
    /**
     * <p>getRole.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public abstract String getRole();

    /**
     * Description of the Method
     *
     * @param s Description of the Parameter
     * @return Description of the Return Value
     */
    protected abstract Element createContent(WebSession s);


    /**
     * Creates a new lessonTracker object.
     *
     * @param props The properties file that was used to persist the user data.
     * @return Description of the Return Value
     */
    public LessonTracker createLessonTracker(Properties props) {

        // If the lesson had any specialized properties in the user persisted properties,
        // now would be the time to pull them out.
        return createLessonTracker();
    }

    /**
     * This allows the screens to provide a custom LessonTracker object if
     * needed.
     *
     * @return Description of the Return Value
     */
    public LessonTracker createLessonTracker() {
        return new LessonTracker();
    }

    /**
     * Gets the lessonTracker attribute of the AbstractLesson object
     *
     * @return The lessonTracker value
     * @param s a {@link org.owasp.webgoat.session.WebSession} object.
     */
    public LessonTracker getLessonTracker(WebSession s) {
        UserTracker userTracker = UserTracker.instance();
        return userTracker.getLessonTracker(s, this);
    }

    /**
     * <p>getLessonTracker.</p>
     *
     * @param s a {@link org.owasp.webgoat.session.WebSession} object.
     * @param userNameOverride a {@link java.lang.String} object.
     * @return a {@link org.owasp.webgoat.session.LessonTracker} object.
     */
    public LessonTracker getLessonTracker(WebSession s, String userNameOverride) {
        UserTracker userTracker = UserTracker.instance();
        return userTracker.getLessonTracker(s, userNameOverride, this);
    }

    /**
     * <p>getLessonTracker.</p>
     *
     * @param s a {@link org.owasp.webgoat.session.WebSession} object.
     * @param lesson a {@link org.owasp.webgoat.lessons.AbstractLesson} object.
     * @return a {@link org.owasp.webgoat.session.LessonTracker} object.
     */
    public LessonTracker getLessonTracker(WebSession s, AbstractLesson lesson) {
        UserTracker userTracker = UserTracker.instance();
        return userTracker.getLessonTracker(s, lesson);
    }

    /**
     * Fill in a descriptive title for this lesson
     *
     * @return The title value
     */
    public abstract String getTitle();

    /**
     * <p>Setter for the field <code>content</code>.</p>
     *
     * @param content a {@link org.apache.ecs.Element} object.
     */
    protected void setContent(Element content) {
        this.content = content;
    }

    /**
     * Description of the Method
     *
     * @return Description of the Return Value
     */
    protected Element makeLogo() {

        return new A("http://www.aspectsecurity.com/webgoat.html", logo);
    }

    /**
     * <p>getSponsor.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSponsor() {
        return "Aspect Security";
    }

    /**
     * <p>getSponsorLogoResource.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSponsorLogoResource() {
        return "images/aspectlogo-horizontal-small.jpg";
    }

    /**
     * Description of the Method
     *
     * @param s Description of the Parameter
     * @return Description of the Return Value
     */
    protected Element makeMessages(WebSession s) {

        if (s == null) {

            return (new StringElement(""));
        }

        Font f = new Font().setColor(HtmlColor.RED);

        String message = s.getMessage();

        f.addElement(message);

        return (f);
    }

    /**
     * Returns the content length of the the html.
     *
     * @return a int.
     */
    public int getContentLength() {
        return getContent().length();
    }

    /**
     * Description of the Method
     *
     * @param out Description of the Parameter
     */
    public void output(PrintWriter out) {

        // format output -- then send to printwriter
        // otherwise we're doing way too much SSL encryption work
        out.print(getContent());

    }

    // hook all the links
    /**
     * <p>Getter for the field <code>content</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getContent() {
        //String makeAllAjax = "<script>goat.utils.makeFormsAjax();goat.utils.ajaxifyAttackHref();</script>";
        // need to do this here as some of the lessons render forms after submission of an ajax form
        return (content == null) ? "" : content.toString();// + makeAllAjax;
    }

    /**
     * Description of the Method
     *
     * @param x Description of the Parameter
     * @return Description of the Return Value
     */
    protected static String pad(int x) {

        StringBuilder sb = new StringBuilder();

        if (x < 10) {

            sb.append(" ");

        }

        if (x < 100) {

            sb.append(" ");

        }

        sb.append(x);

        return (sb.toString());
    }

    /**
     * Description of the Method
     *
     * @param token Description of the Parameter
     * @return Description of the Return Value
     */
    protected static String convertMetachars(String token) {

        int mci = 0;

        /*
         * meta char array FIXME: Removed the conversion of whitespace " " to "&nbsp" in order for
         * the html to be automatically wrapped in client browser. It is better to add line length
         * checking and only do "&nbsp" conversion in lines that won't exceed screen size, say less
         * than 80 characters.
         */
        String[] metaChar = {"&", "<", ">", "\"", "\t", System.getProperty("line.separator")};

        String[] htmlCode = {"&amp;", "&lt;", "&gt;", "&quot;", "    ", "<br>"};

        String replacedString = token;
        for (; mci < metaChar.length; mci += 1) {
            replacedString = replacedString.replaceAll(metaChar[mci], htmlCode[mci]);
        }
        return (replacedString);
    }

    /**
     * Description of the Method
     *
     * @param token Description of the Parameter
     * @return Description of the Return Value
     */
    protected static String convertMetacharsJavaCode(String token) {
        return (convertMetachars(token).replaceAll(" ", "&nbsp;"));
    }

    /**
     * Description of the Method
     *
     * @param s Description of the Parameter
     * @return Description of the Return Value
     */
	// protected abstract Element wrapForm( WebSession s );
}
