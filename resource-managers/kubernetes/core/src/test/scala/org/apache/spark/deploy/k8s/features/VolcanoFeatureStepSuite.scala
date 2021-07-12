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

package org.apache.spark.deploy.k8s.features

import sh.volcano.scheduling.PodGroup

import org.apache.spark.SparkFunSuite
import org.apache.spark.deploy.k8s._

class VolcanoFeatureStepSuite extends SparkFunSuite {

  test("assign driver pod with volcano") {
    val kubernetesConf = KubernetesTestConf.createDriverConf()
    val step = new VolcanoFeatureStep(kubernetesConf)
    val configuredPod = step.configurePod(SparkPod.initialPod())

    val annotation = configuredPod.pod.getMetadata().getAnnotations

    assert(configuredPod.pod.getSpec.getSchedulerName === "volcano")
    assert(annotation.get("volcano.sh/task-spec") === "spark-driver")
  }

  test("assign executor pod with volcano") {
    val kubernetesConf = KubernetesTestConf.createExecutorConf()
    val step = new VolcanoFeatureStep(kubernetesConf)
    val configuredPod = step.configurePod(SparkPod.initialPod())

    val annotation = configuredPod.pod.getMetadata().getAnnotations

    assert(configuredPod.pod.getSpec.getSchedulerName === "volcano")
    assert(annotation.get("volcano.sh/task-spec") === "spark-executor")
  }

  test("support podgroup") {
    val kubernetesConf = KubernetesTestConf.createDriverConf()
    val step = new VolcanoFeatureStep(kubernetesConf)
    val configuredPod = step.configurePod(SparkPod.initialPod())
    val resources = step.getAdditionalKubernetesResources()
    val annotation = configuredPod.pod.getMetadata().getAnnotations

    assert(resources.length == 1)
    val podgroup = resources.head.asInstanceOf[PodGroup]

    assert(annotation.get("scheduling.k8s.io/group-name") === podgroup.getMetadata().getName)
    assert(podgroup.getSpec().getQueue === "default")
  }
}