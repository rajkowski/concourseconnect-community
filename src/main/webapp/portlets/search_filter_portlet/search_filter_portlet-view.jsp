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
<%--@elvariable id="searchBean" type="com.concursive.connect.web.modules.search.beans.SearchBean"--%>
<jsp:useBean id="searchBean" class="com.concursive.connect.web.modules.search.beans.SearchBean" scope="request" />
<portlet:defineObjects/>
<c:set var="ctx" value="${renderRequest.contextPath}" scope="request"/>
<h3>Filter By</h3>
<ul>
  <li>
    <a href="${ctx}<%= searchBean.getUrlByFilter(-1) %>" title="Filter search results by best match"<c:if test="${searchBean.filter == -1}"> class="active"</c:if>>
      <em>Best Match</em>
    </a>
  </li>
  <li>
    <a href="${ctx}<%= searchBean.getUrlByFilter(1) %>" title="Filter search results by highest rated"<c:if test="${searchBean.filter == 1}"> class="active"</c:if>>
      <em>Highest Rated</em>
    </a>
  </li>
  <li>
    <a href="${ctx}<%= searchBean.getUrlByFilter(2) %>" title="Filter search results by most reviewed"<c:if test="${searchBean.filter == 2}"> class="active"</c:if>>
      <em>Most Reviewed</em>
    </a>
  </li>
  <li>
    <a href="${ctx}<%= searchBean.getUrlByFilter(3) %>" title="Filter search results by newly added"<c:if test="${searchBean.filter == 3}"> class="active"</c:if>>
      <em>Newly Added</em>
    </a>
  </li>
<%--
  <li class="searchFilterListItem-Nearby"><a href="${ctx}<%= searchBean.getUrlByFilter(4) %>" title="Filter search by nearby">Nearby</a></li>
  <li class="searchFilterListItem-MostPopular"><a href="${ctx}<%= searchBean.getUrlByFilter(5) %>" title="Filter search results by most popular">Most Popular</a></li>
--%>
</ul>

