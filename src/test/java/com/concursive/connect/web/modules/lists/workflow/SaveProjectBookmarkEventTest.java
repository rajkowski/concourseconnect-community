/*
 * ConcourseConnect
 * Copyright 2009 Concursive Corporation
 * http://www.concursive.com
 *
 * This file is part of ConcourseConnect, an open source social business
 * software and community platform.
 *
 * Concursive ConcourseConnect is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, version 3 of the License.
 *
 * Under the terms of the GNU Affero General Public License you must release the
 * complete source code for any application that uses any part of ConcourseConnect
 * (system header files and libraries used by the operating system are excluded).
 * These terms must be included in any work that has ConcourseConnect components.
 * If you are developing and distributing open source applications under the
 * GNU Affero General Public License, then you are free to use ConcourseConnect
 * under the GNU Affero General Public License.
 *
 * If you are deploying a web site in which users interact with any portion of
 * ConcourseConnect over a network, the complete source code changes must be made
 * available.  For example, include a link to the source archive directly from
 * your web site.
 *
 * For OEMs, ISVs, SIs and VARs who distribute ConcourseConnect with their
 * products, and do not license and distribute their source code under the GNU
 * Affero General Public License, Concursive provides a flexible commercial
 * license.
 *
 * To anyone in doubt, we recommend the commercial license. Our commercial license
 * is competitively priced and will eliminate any confusion about how
 * ConcourseConnect can be used and distributed.
 *
 * ConcourseConnect is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with ConcourseConnect.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Attribution Notice: ConcourseConnect is an Original Work of software created
 * by Concursive Corporation
 */
package com.concursive.connect.web.modules.lists.workflow;

import com.concursive.commons.workflow.AbstractWorkflowManagerTest;
import com.concursive.commons.workflow.BusinessProcess;
import com.concursive.connect.Constants;
import com.concursive.connect.cache.utils.CacheUtils;
import com.concursive.connect.web.modules.activity.dao.ProjectHistory;
import com.concursive.connect.web.modules.activity.dao.ProjectHistoryList;
import com.concursive.connect.web.modules.lists.dao.Task;
import com.concursive.connect.web.modules.lists.dao.TaskCategory;
import com.concursive.connect.web.modules.lists.dao.TaskList;
import com.concursive.connect.web.modules.login.dao.User;
import com.concursive.connect.web.modules.login.utils.UserUtils;
import com.concursive.connect.web.modules.profile.dao.Project;
import com.concursive.connect.web.modules.profile.dao.ProjectList;
import com.concursive.connect.web.modules.profile.utils.ProjectUtils;
import com.concursive.connect.web.modules.wiki.utils.WikiLink;
import com.concursive.connect.web.utils.LookupList;

import java.sql.Timestamp;

/**
 * Tests to see if project bookmark events are being recorded..
 *
 * @author Ananth
 * @created Feb 22, 2009
 */
public class SaveProjectBookmarkEventTest extends AbstractWorkflowManagerTest {
  protected static final int GROUP_ID = 1;
  protected static final String PROCESS_NAME = "teamelements.application.project.favorite-list";

  protected final static String HISTORY_BOOKMARK_SINGLELIST_TEXT = "history.bookmark.singlelist.text";
  protected final static String HISTORY_BOOKMARK_MULTIPLELIST_TEXT = "history.bookmark.multiplelist.text";

  public void testSaveProjectBookmarkEvent() throws Exception {
    // Insert project
    Project project = new Project();
    project.setTitle("Project SQL Test");
    project.setShortDescription("Project SQL Test Description");
    project.setRequestDate(new Timestamp(System.currentTimeMillis()));
    project.setEstimatedCloseDate((Timestamp) null);
    project.setRequestedBy("Project SQL Test Requested By");
    project.setRequestedByDept("Project SQL Test Requested By Department");
    project.setBudgetCurrency("USD");
    project.setBudget("10000.75");
    project.setGroupId(GROUP_ID);
    project.setEnteredBy(USER_ID);
    project.setModifiedBy(USER_ID);
    assertNotNull(project);
    boolean result = project.insert(db);
    assertTrue("Project was not inserted", result);

    //Insert project that has lists
    Project projectWithLists = new Project();
    projectWithLists.setTitle("Project SQL Test");
    projectWithLists.setShortDescription("Project SQL Test Description");
    projectWithLists.setRequestDate(new Timestamp(System.currentTimeMillis()));
    projectWithLists.setEstimatedCloseDate((Timestamp) null);
    projectWithLists.setRequestedBy("Project SQL Test Requested By");
    projectWithLists.setRequestedByDept("Project SQL Test Requested By Department");
    projectWithLists.setBudgetCurrency("USD");
    projectWithLists.setBudget("10000.75");
    projectWithLists.setGroupId(GROUP_ID);
    projectWithLists.setEnteredBy(USER_ID);
    projectWithLists.setModifiedBy(USER_ID);
    assertNotNull(projectWithLists);
    boolean res = projectWithLists.insert(db);
    assertTrue("Project with lists was not inserted", res);

    //Add a task category list
    TaskCategory list = new TaskCategory();
    list.setLinkModuleId(Constants.TASK_CATEGORY_PROJECTS);
    list.setLinkItemId(projectWithLists.getId());
    list.setDescription("Temp List");
    list.insert(db);
    assertTrue("Unable to add a list", list.getId() > -1);

    TaskList taskList = new TaskList();

    LookupList taskPriorityList = CacheUtils.getLookupList("lookup_task_priority");

    //Add a task to a particular task category
    Task task = new Task();
    task.setEnteredBy(USER_ID);
    task.setOwner(USER_ID);
    task.setDescription(project.getTitle());
    task.setModifiedBy(USER_ID);
    task.setProjectId(projectWithLists.getId());
    task.setLinkModuleId(Constants.TASK_CATEGORY_PROJECTS);
    task.setLinkItemId(project.getId());
    task.setCategoryId(list.getId());
    task.setPriority(taskPriorityList.getIdFromValue("1"));
    boolean recordInserted = task.insert(db);
    assertTrue("Unable to add a task", recordInserted);

    taskList.add(task);

    User user = UserUtils.loadUser(task.getOwner());
    Project userProfile = ProjectUtils.loadProject(user.getProfileProjectId());

    BusinessProcess thisProcess = processList.get(PROCESS_NAME);
    assertTrue("Process not found...", thisProcess != null);
    context.setPreviousObject(null);
    context.setThisObject(taskList);
    context.setProcessName(PROCESS_NAME);
    context.setProcess(thisProcess);
    context.setAttribute("userId", new Integer(USER_ID));
    context.setAttribute("user", WikiLink.generateLink(userProfile));
    context.setAttribute("profile", WikiLink.generateLink(project));
    context.setAttribute("list", WikiLink.generateLink(list));

    workflowManager.execute(context);

    //Now query the database and test to see if a history item was added for the specified test user Id.
    ProjectHistoryList historyList = new ProjectHistoryList();
    historyList.setProjectId(projectWithLists.getId());
    historyList.setEnteredBy(USER_ID);
    historyList.setLinkObject(ProjectHistoryList.LIST_OBJECT);
    historyList.setEventType(ProjectHistoryList.BOOKMARK_PROFILE_EVENT);
    historyList.setLinkItemId(project.getId());
    historyList.buildList(db);
    assertTrue("History event was not recorded!", historyList.size() == 1);

    ProjectHistory history = historyList.get(0);
    assertEquals("Recorded event mismatch",
        "[[|" + userProfile.getId() + ":profile||" + userProfile.getTitle() + "]] bookmarked [[|" + project.getId() + ":profile||Project SQL Test]] on [[|" + userProfile.getId() + ":list|" + list.getId() + "|Temp List]]",
        history.getDescription());

    //Delete the history item because the test is done (and any other needed objects)
    history.delete(db);
    //Delete the task
    task.delete(db);

    //Delete the task category
    list.delete(db);

    // Delete the projects
    boolean deleteResult = projectWithLists.delete(db, (String) null);
    assertTrue("Project with lists was not deleted", deleteResult);

    deleteResult = project.delete(db, (String) null);
    assertTrue("Project wasn't deleted", deleteResult);
    // Try to find the previously deleted project
    ProjectList projectList = new ProjectList();
    projectList.setProjectId(project.getId());
    projectList.buildList(db);
    assertTrue("Project exists when it shouldn't -- id " + project.getId(), projectList.size() == 0);
  }
}
