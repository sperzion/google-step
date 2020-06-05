// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import static java.util.stream.Collectors.toList;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import com.google.sps.data.Comment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private static final String PARAM_NEW_COMMENT = "new-comment";
  private static final String ENTITY_COMMENT = "Comment";
  private static final String PROPERTY_CONTENT = "content";
  private static final String PROPERTY_TIMESTAMP = "timestamp";

  private final Gson gson = new Gson();
  private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  private final List<String> comments = new ArrayList<>();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query(ENTITY_COMMENT).addSort(PROPERTY_TIMESTAMP, SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);

    List<Comment> comments = StreamSupport.stream(results.asIterable().spliterator(), false)
      .map(entity -> new Comment(
          (String) entity.getProperty(PROPERTY_CONTENT),
          (long) entity.getProperty(PROPERTY_TIMESTAMP)))
      .collect(toList());

    String json = gson.toJson(comments);
    response.setContentType("text/json;");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String newComment = getCommentFrom(request);
    if (newComment.length() <= 0) {
      response.setContentType("text/html");
      response.getWriter().println("Please enter some text for your comment.");
      return;
    }

    Entity commentEntity = new Entity(ENTITY_COMMENT);
    commentEntity.setProperty(PROPERTY_CONTENT, newComment);
    commentEntity.setProperty(PROPERTY_TIMESTAMP, System.currentTimeMillis());

    datastore.put(commentEntity);
    comments.add(newComment);
    response.sendRedirect("/index.html");
  }

  private static String getCommentFrom(HttpServletRequest request) {
    return request.getParameter(PARAM_NEW_COMMENT);
  }
}
