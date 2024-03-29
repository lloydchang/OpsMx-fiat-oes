/*
 * Copyright 2022 OpsMx, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.spinnaker.fiat.model.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.netflix.spinnaker.fiat.model.Authorization;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@EqualsAndHashCode(
    callSuper = false,
    of = {"resourceType", "name"})
public class Pipeline extends BaseAccessControlled<Pipeline> implements Viewable {
  final ResourceType resourceType = ResourceType.PIPELINE;

  private String name;

  private Permissions permissions = Permissions.EMPTY;

  @JsonIgnore
  public View getView(Set<Role> userRoles, boolean isAdmin) {
    return new View(this, userRoles, isAdmin);
  }

  @Data
  @EqualsAndHashCode(callSuper = false)
  @NoArgsConstructor
  public static class View extends Viewable.BaseView implements Authorizable {
    String name;
    Set<Authorization> authorizations;

    public View(Pipeline pipeline, Set<Role> userRoles, boolean isAdmin) {
      this.name = pipeline.name;
      if (isAdmin) {
        this.authorizations = Authorization.ALL;
      } else {
        this.authorizations = pipeline.permissions.getAuthorizations(userRoles);
      }
    }
  }
}
