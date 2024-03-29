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
import com.netflix.spinnaker.fiat.model.UserPermission;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Data
@EqualsAndHashCode(
    callSuper = false,
    of = {"resourceType", "name"})
public class ServiceAccount implements Resource, Viewable {
  private final ResourceType resourceType = ResourceType.SERVICE_ACCOUNT;

  private String name;
  private Set<String> memberOf = new LinkedHashSet<>();

  public UserPermission toUserPermission() {
    val roles =
        memberOf.stream()
            .filter(StringUtils::hasText)
            .map(membership -> new Role(membership).setSource(Role.Source.EXTERNAL))
            .collect(Collectors.toSet());
    return new UserPermission().setId(name).setRoles(roles);
  }

  public ServiceAccount setMemberOf(List<String> membership) {
    memberOf =
        Optional.ofNullable(membership)
            .map(Collection::stream)
            .orElseGet(Stream::empty)
            .map(String::trim)
            .map(String::toLowerCase)
            .collect(Collectors.toSet());
    return this;
  }

  @JsonIgnore
  @Override
  public View getView(Set<Role> ignored, boolean isAdmin) {
    return new View(this);
  }

  @Data
  @EqualsAndHashCode(callSuper = false)
  @NoArgsConstructor
  public static class View extends BaseView {
    private String name;
    private List<String> memberOf;

    public View(ServiceAccount serviceAccount) {
      this.name = serviceAccount.name;
      this.memberOf = new ArrayList<>(serviceAccount.memberOf);
    }
  }
}
