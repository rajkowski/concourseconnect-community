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

import com.concursive.commons.workflow.ComponentContext;
import com.concursive.commons.workflow.ComponentInterface;
import com.concursive.commons.workflow.ObjectHookComponent;
import com.concursive.connect.Constants;
import com.concursive.connect.web.modules.lists.dao.ListsTemplate;
import com.concursive.connect.web.modules.lists.dao.ListsTemplateList;
import com.concursive.connect.web.modules.lists.dao.TaskCategory;
import com.concursive.connect.web.modules.profile.dao.Project;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * Adds a lists to the project if the source template exists
 *
 * @author Kailash Bhoopalam
 * @created Aug 13, 2008
 */
public class AttachListsFromTemplate extends ObjectHookComponent implements ComponentInterface {

  public String getDescription() {
    return "Adds a lists to the project if the source template exists";
  }

  public boolean execute(ComponentContext context) {
    // Parameters

    // Test and execute
    Connection db = null;
    Project thisProject = (Project) context.getThisObject();
    if (thisProject.getCategoryId() < 1) {
      return false;
    } else {
      try {
        db = getConnection(context);
        // Locate the template
        ListsTemplateList templateList = new ListsTemplateList();
        templateList.setProjectCategoryId(thisProject.getCategoryId());
        templateList.setEnabled(true);
        templateList.buildList(db);
        if (templateList.size() == 0) {
          return false;
        }
        // insert lists for project
        int level = 0;
        for (ListsTemplate template : templateList) {
          ArrayList<String> listNames = template.getListNamesArrayList();
          for (String listName : listNames) {
            TaskCategory newCategory = new TaskCategory();
            newCategory.setLinkModuleId(Constants.TASK_CATEGORY_PROJECTS);
            newCategory.setLinkItemId(thisProject.getId());
            newCategory.setDescription(listName);
            level = level + 10;
            newCategory.setLevel(level);
            newCategory.insert(db);
          }
        }
        return true;
      } catch (Exception e) {
        e.printStackTrace(System.out);
      } finally {
        freeConnection(context, db);
      }
    }
    return false;
  }
}