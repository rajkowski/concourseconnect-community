<%--
  ~ ConcourseConnect
  ~ Copyright 2009 Concursive Corporation
  ~ http://www.concursive.com
  ~
  ~ This file is part of ConcourseConnect, an open source social business
  ~ software and community platform.
  ~
  ~ Concursive ConcourseConnect is free software: you can redistribute it and/or
  ~ modify it under the terms of the GNU Affero General Public License as published
  ~ by the Free Software Foundation, version 3 of the License.
  ~
  ~ Under the terms of the GNU Affero General Public License you must release the
  ~ complete source code for any application that uses any part of ConcourseConnect
  ~ (system header files and libraries used by the operating system are excluded).
  ~ These terms must be included in any work that has ConcourseConnect components.
  ~ If you are developing and distributing open source applications under the
  ~ GNU Affero General Public License, then you are free to use ConcourseConnect
  ~ under the GNU Affero General Public License.
  ~
  ~ If you are deploying a web site in which users interact with any portion of
  ~ ConcourseConnect over a network, the complete source code changes must be made
  ~ available.  For example, include a link to the source archive directly from
  ~ your web site.
  ~
  ~ For OEMs, ISVs, SIs and VARs who distribute ConcourseConnect with their
  ~ products, and do not license and distribute their source code under the GNU
  ~ Affero General Public License, Concursive provides a flexible commercial
  ~ license.
  ~
  ~ To anyone in doubt, we recommend the commercial license. Our commercial license
  ~ is competitively priced and will eliminate any confusion about how
  ~ ConcourseConnect can be used and distributed.
  ~
  ~ ConcourseConnect is distributed in the hope that it will be useful, but WITHOUT
  ~ ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
  ~ FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more
  ~ details.
  ~
  ~ You should have received a copy of the GNU Affero General Public License
  ~ along with ConcourseConnect.  If not, see <http://www.gnu.org/licenses/>.
  ~
  ~ Attribution Notice: ConcourseConnect is an Original Work of software created
  ~ by Concursive Corporation
  --%>
<%@ taglib uri="/WEB-INF/portlet.tld" prefix="portlet" %>
<%@ taglib uri="/WEB-INF/concourseconnect-taglib.tld" prefix="ccp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import="java.util.*" %>
<%@ include file="../../initPage.jsp" %>
<portlet:defineObjects/>
<c:set var="ctx" value="${renderRequest.contextPath}" scope="request"/>
  <h3><c:out value="${title}"/></h3>
  <c:if test="${!empty invitedProjectList}">
    <ol>
      There
      <c:choose>
        <c:when test="${fn:length(invitedProjectList) == 1}">
          is 1 invite
        </c:when>
        <c:otherwise>
          are <c:out value="${fn:length(invitedProjectList)}" /> invites
        </c:otherwise>
      </c:choose>
      pending.
      <br />
      <c:forEach items="${invitedProjectList}" var="invitedProject">
        <h4><a href="${ctx}/show/${invitedProject.uniqueId}" title="<c:out value="${invitedProject.title}" />"><c:out value="${invitedProject.title}"/></a></h4>
        <c:if test="${updateInvitations eq 'true'}">
          <portlet:renderURL var="acceptURL" portletMode="view" windowState="maximized">
            <portlet:param name="projectId" value="${invitedProject.id}"/>
            <portlet:param name="accept" value="true"/>
            <portlet:param name="viewType" value="setAccept"/>
          </portlet:renderURL>
          <portlet:renderURL var="declineURL" portletMode="view" windowState="maximized">
            <portlet:param name="projectId" value="${invitedProject.id}"/>
            <portlet:param name="accept" value="false"/>
            <portlet:param name="viewType" value="setAccept"/>
          </portlet:renderURL>
          <c:set var="acceptInvitationURL" scope="page">
            javascript:copyRequest('<%= pageContext.getAttribute("acceptURL") %>&out=text','<portlet:namespace/>acceptOrDecline_${invitedProject.id}','acceptedInvitation_${invitedProject.id}');
          </c:set>
          <c:set var="declineInvitationURL" scope="page">
            javascript:copyRequest('<%= pageContext.getAttribute("declineURL") %>&out=text','<portlet:namespace/>acceptOrDecline_${invitedProject.id}','declinedInvitation_${invitedProject.id}');
          </c:set>
          <div id="acceptedInvitation_${invitedProject.id}" class="menu">Accepted</div>
          <div id="declinedInvitation_${invitedProject.id}" class="menu">Declined</div>
          <div id="<portlet:namespace/>acceptOrDecline_${invitedProject.id}"><a href="${acceptInvitationURL}">Accept</a> or <a href="${declineInvitationURL}">Decline</a></div>
        </c:if>
        <br />
      </c:forEach>
    </ol>
  </c:if>
  <c:if test="${empty invitedProjectList}">
    <p>There are no pending invitations</p>
  </c:if>
