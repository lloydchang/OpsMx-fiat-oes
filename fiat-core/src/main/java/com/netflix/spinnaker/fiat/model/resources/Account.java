/*
 * Copyright 2016 Google, Inc.
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
    of = {"resourceType", "cloudProvider", "name"})
public class Account extends BaseAccessControlled<Account> implements Viewable {
  final ResourceType resourceType = ResourceType.ACCOUNT;

  private String name;
  private String cloudProvider;
  private Permissions permissions = Permissions.EMPTY;

  @JsonIgnore
  @Override
  public View getView(Set<Role> userRoles, boolean isAdmin) {
    return new View(this, userRoles, isAdmin);
  }

  @Data
  @EqualsAndHashCode(callSuper = false)
  @NoArgsConstructor
  public static class View extends BaseView implements Authorizable {
    String name;
    Set<Authorization> authorizations;

    public View(Account account, Set<Role> userRoles, boolean isAdmin) {
      this.name = account.name;
      if (isAdmin) {
        this.authorizations = Authorization.ALL;
      } else {
        this.authorizations = account.permissions.getAuthorizations(userRoles);
      }
    }
  }
}
