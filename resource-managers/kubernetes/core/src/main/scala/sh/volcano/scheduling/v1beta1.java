package sh.volcano.scheduling;

import io.fabric8.kubernetes.api.model.apiextensions.v1beta1.CustomResourceDefinition;
import io.fabric8.kubernetes.api.model.apiextensions.v1beta1.CustomResourceDefinitionBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.NonNamespaceOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.fabric8.kubernetes.internal.KubernetesDeserializer;

public class v1beta1 {
    private static String resourceScope(boolean resourceNamespaced) {
        if (resourceNamespaced) {
            return "Namespaced";
        }
        return "Cluster";
    }

    public static final String POD_GROUP_ANNOTATION = "scheduling.k8s.io/group-name";
    public static final String VOLCANO_TASK_SPEC = "volcano.sh/task-spec";
    public static final String VOLCANO_ROLE_DRIVER = "spark-driver";
    public static final String VOLCANO_ROLE_EXECUTOR = "spark-executor";

    public static final String POD_GROUP = "scheduling.volcano.sh";
    public static final String POD_GROUP_CRD_NAME = "podgroups." + POD_GROUP;
    public static final String POD_GROUP_KIND = "PodGroup";
    public static final String POD_GROUP_LIST_KIND = "PodGroupList";
    public static final String POD_GROUP_PLURAL = "podgroups";
    public static final String POD_GROUP_SINGULAR = "podgroup";
    public static final String POD_GROUP_APIVERSION = "v1beta1";
    public static final String POD_GROUP_GROUP_APIVERSION = POD_GROUP + "/" + POD_GROUP_APIVERSION;

    public static NonNamespaceOperation<PodGroup, PodGroupList, Resource<PodGroup>> getClient(KubernetesClient client, String namespace) {
        NonNamespaceOperation<PodGroup, PodGroupList, Resource<PodGroup>> podGroupClient = client.customResources(PodGroup.class, PodGroupList.class);
        podGroupClient = ((MixedOperation<PodGroup, PodGroupList, Resource<PodGroup>>) podGroupClient).inNamespace(namespace);
        return podGroupClient;
    }

    {
        KubernetesDeserializer.registerCustomKind(POD_GROUP_GROUP_APIVERSION, POD_GROUP_KIND, PodGroup.class);
        KubernetesDeserializer.registerCustomKind(POD_GROUP_GROUP_APIVERSION, POD_GROUP_LIST_KIND, PodGroupList.class);
    }
}