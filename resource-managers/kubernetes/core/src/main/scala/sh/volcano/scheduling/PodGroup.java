/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sh.volcano.scheduling;

import io.fabric8.kubernetes.client.CustomResource;

/**
 */
public class PodGroup extends CustomResource {
    private PodGroupSpec spec;
    private PodGroupStatus status;

    {
        this.setApiVersion(v1beta1.POD_GROUP_GROUP_APIVERSION);
        this.setKind(v1beta1.POD_GROUP_KIND);
    }

    @Override
    public String toString() {
        return "PodGroup{" +
                "apiVersion='" + getApiVersion() + '\'' +
                ", metadata=" + getMetadata() +
                ", spec=" + spec +
                ", spec=" + status +
                '}';
    }

    public PodGroupSpec getSpec() {
        return spec;
    }

    public void setSpec(PodGroupSpec spec) {
        this.spec = spec;
    }

    public PodGroupStatus getStatus() {
        return status;
    }

    public void setStatus(PodGroupStatus status) {
        this.status = status;
    }
}