/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.tajo.worker;

import org.apache.tajo.ResourceProtos.TaskStatusProto;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;

public interface Task {

  void init() throws IOException;

  void fetch(ExecutorService fetcherExecutor);

  void run() throws Exception;

  void kill();

  void abort();

  void cleanup();

  boolean hasFetchPhase();

  boolean isProgressChanged();

  boolean isStopped();

  void updateProgress();

  TaskAttemptContext getTaskContext();

  ExecutionBlockContext getExecutionBlockContext();

  TaskStatusProto getReport();

  TaskHistory createTaskHistory();

  List<AbstractFetcher> getFetchers();
}
