
package org.owasp.webgoat.plugin;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.A;
import org.apache.ecs.html.B;
import org.apache.ecs.html.Comment;
import org.apache.ecs.html.H1;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.P;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TH;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.LessonAdapter;
import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.WebSession;

import java.util.ArrayList;
import java.util.List;


/***************************************************************************************************
 * 
 * 
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * 
 * Copyright (c) 2002 - 20014 Bruce Mayhew
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 * 
 * Getting Source ==============
 * 
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software
 * projects.
 * 
 * For details, please see http://webgoat.github.io
 * 
 * @author Jeff Williams <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @created October 28, 2003
 */
public class HtmlClues extends LessonAdapter
{
    /**
     * Description of the Field
     */
    protected final static String PASSWORD = "Password";

    /**
     * Description of the Field
     */
    protected final static String USERNAME = "Username";
    
    /**
     * First time in
     */
    private static boolean FIRST_TIME = true;

    /**
     * Description of the Method
     * 
     * @param s
     *            Description of the Parameter
     * @return Description of the Return Value
     */
    private boolean backdoor(WebSession s)
    {
        String username = s.getParser().getRawParameter(USERNAME, "");
        String password = s.getParser().getRawParameter(PASSWORD, "");

        // <START_OMIT_SOURCE>
        return (username.equals("admin") && password.equals("adminpw"));
        // <END_OMIT_SOURCE>
    }

    /**
     * Description of the Method
     * 
     * @param s
     *            Description of the Parameter
     * @return Description of the Return Value
     */
    protected Element createContent(WebSession s)
    {
        ElementContainer ec = new ElementContainer();

        try
        {
            // <START_OMIT_SOURCE>
            ec.addElement(new Comment("FIXME admin:adminpw"));
            // <END_OMIT_SOURCE>
            ec.addElement(new Comment("Use Admin to regenerate database"));

            
            if (backdoor(s))
            {
                makeSuccess(s);

                s.setMessage(getLabelManager().get("HtmlCluesBINGO"));
                ec.addElement(makeUser(s, "admin", "CREDENTIALS"));
            }
            else
            {
            	if (!FIRST_TIME)
            	{
            		s.setMessage(getLabelManager().get("HtmlCluesLoginFailed"));
            	}
        		FIRST_TIME= false;
                ec.addElement(makeLogin(s));
            }
        } catch (Exception e)
        {
            s.setMessage("Error generating " + this.getClass().getName());
        }

        return (ec);
    }

    /**
     * Initiates lesson restart functionality
     */
    public void restartLesson()
    {
    	FIRST_TIME=true;
    }
    /**
     * Description of the Method
     * 
     * @param s
     *            Description of the Parameter
     * @param user
     *            Description of the Parameter
     * @param method
     *            Description of the Parameter
     * @return Description of the Return Value
     * @exception Exception
     *                Description of the Exception
     */
    protected Element makeUser(WebSession s, String user, String method) throws Exception
    {
        ElementContainer ec = new ElementContainer();
        ec.addElement(new P().addElement(getLabelManager().get("WelcomeUser")+ user));
        ec.addElement(new P().addElement(getLabelManager().get("YouHaveBeenAuthenticatedWith") + method));

        return (ec);
    }

    protected Element makeLogin(WebSession s)
    {
        ElementContainer ec = new ElementContainer();

        ec.addElement(new H1().addElement("Sign In "));
        Table t = new Table().setCellSpacing(0).setCellPadding(2).setBorder(0).setWidth("90%").setAlign("center");

        if (s.isColor())
        {
            t.setBorder(1);
        }

        TR tr = new TR();
        tr.addElement(new TH()
                .addElement(getLabelManager().get("WeakAuthenticationCookiePleaseSignIn"))
                .setColSpan(2).setAlign("left"));
        t.addElement(tr);

        tr = new TR();
        tr.addElement(new TD().addElement("*"+getLabelManager().get("RequiredFields")).setWidth("30%"));
        t.addElement(tr);

        tr = new TR();
        tr.addElement(new TD().addElement("&nbsp;").setColSpan(2));
        t.addElement(tr);

        TR row1 = new TR();
        TR row2 = new TR();
        row1.addElement(new TD(new B(new StringElement("*"+getLabelManager().get("UserName")+": "))));
        row2.addElement(new TD(new B(new StringElement("*"+getLabelManager().get("Password")+": "))));

        Input input1 = new Input(Input.TEXT, USERNAME, "");
        Input input2 = new Input(Input.PASSWORD, PASSWORD, "");
        row1.addElement(new TD(input1));
        row2.addElement(new TD(input2));
        t.addElement(row1);
        t.addElement(row2);

        Element b = ECSFactory.makeButton(getLabelManager().get("Login"));
        t.addElement(new TR(new TD(b)));
        ec.addElement(t);

        return (ec);
    }

    /**
     * Gets the hints attribute of the CluesScreen object
     * 
     * @return The hints value
     */
    protected List<String> getHints(WebSession s)
    {
        List<String> hints = new ArrayList<String>();
        hints.add(getLabelManager().get("HtmlCluesHint1"));
        hints.add(getLabelManager().get("HtmlCluesHint2"));
        hints.add(getLabelManager().get("HtmlCluesHint3"));
        
        return hints;
    }



    private final static Integer DEFAULT_RANKING = new Integer(30);

    protected Integer getDefaultRanking()
    {
        return DEFAULT_RANKING;
    }

    /**
     * Gets the category attribute of the FailOpenAuthentication object
     * 
     * @return The category value
     */
    protected Category getDefaultCategory()
    {
        return Category.CODE_QUALITY;
    }

    /**
     * Gets the title attribute of the CluesScreen object
     * 
     * @return The title value
     */
    public String getTitle()
    {
        return ("Discover Clues in the HTML");
    }

}
