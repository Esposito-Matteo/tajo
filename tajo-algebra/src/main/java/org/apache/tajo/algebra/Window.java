/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.tajo.algebra;

import com.google.common.base.Objects;
import org.apache.tajo.util.TUtil;

public class Window extends UnaryOperator {
  private WindowDefinition [] definitions;

  public Window(WindowDefinition [] definitions) {
    super(OpType.Window);
    this.definitions = definitions;
  }

  public WindowDefinition [] getWindowDefinitions() {
    return definitions;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(definitions);
  }

  @Override
  boolean equalsTo(Expr expr) {
    Window another = (Window) expr;
    return TUtil.checkEquals(definitions, another.definitions);
  }

  public static class WindowDefinition {
    private String windowName;
    private WindowSpecExpr windowSpec;

    public WindowDefinition(String windowName, WindowSpecExpr spec) {
      this.windowName = windowName;
      this.windowSpec = spec;
    }

    public String getWindowName() {
      return windowName;
    }

    public WindowSpecExpr getWindowSpec() {
      return windowSpec;
    }
  }
}
