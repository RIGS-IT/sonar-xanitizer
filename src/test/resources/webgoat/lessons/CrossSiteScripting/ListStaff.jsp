<%@ page contentType="text/html; charset=ISO-8859-1" language="java" 
	import="java.util.*, org.owasp.webgoat.session.*, org.owasp.webgoat.lessons.CrossSiteScripting.CrossSiteScripting" 
	errorPage="" %>
<%
	WebSession webSession = ((WebSession)session.getAttribute("websession"));
	int myUserId = webSession.getUserIdInLesson();
%>
	<div class="lesson_title_box"><strong>Welcome Back </strong><span class="lesson_text_db"><%=webSession.getUserNameInLesson()%></span> - Staff Listing Page</div>
		<br>
		<br>
		<br>
		<p>Select from the list below	</p>
		<form id="form1" name="form1" method="post" action="<%=webSession.getCurrentLesson().getFormAction()%>">
  <table width="60%" border="0" cellpadding="3">
    <tr>
      <td>  <label>
  <select name="<%=CrossSiteScripting.EMPLOYEE_ID%>" size="11">
			      	<%
			      	List employees = (List) session.getAttribute("CrossSiteScripting." + CrossSiteScripting.STAFF_ATTRIBUTE_KEY);
			      	Iterator i = employees.iterator();
			      	EmployeeStub stub = (EmployeeStub) i.next();%>
			      	<option selected value="<%=Integer.toString(stub.getId())%>"><%=stub.getFirstName() + " " + stub.getLastName()+ " (" + stub.getRole() + ")"%></option><%
					while (i.hasNext())
					{
						stub = (EmployeeStub) i.next();%>
						<option value="<%=Integer.toString(stub.getId())%>"><%=stub.getFirstName() + " " + stub.getLastName()+ " (" + stub.getRole() + ")"%></option><%
					}%>
  </select>
  </label></td>
      <td>
	        	<input type="submit" name="action" value="<%=CrossSiteScripting.SEARCHSTAFF_ACTION%>"/><br>
	        	<input type="submit" name="action" value="<%=CrossSiteScripting.VIEWPROFILE_ACTION%>"/><br>
            		<% 
				if (webSession.isAuthorizedInLesson(myUserId, CrossSiteScripting.CREATEPROFILE_ACTION))
				{
				%>
					<input type="submit" disabled name="action" value="<%=CrossSiteScripting.CREATEPROFILE_ACTION%>"/><br>
				<% 
				}
				%>
            		<% 
				if (webSession.isAuthorizedInLesson(myUserId, CrossSiteScripting.DELETEPROFILE_ACTION))
				{
				%>
					<input type="submit" name="action" value="<%=CrossSiteScripting.DELETEPROFILE_ACTION%>"/><br>
				<% 
				}
				%>
			<br>
					<input type="submit" name="action" value="<%=CrossSiteScripting.LOGOUT_ACTION%>"/>
	  </td>
    </tr>
  </table>

		</form>
		
