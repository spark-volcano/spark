package sh.volcano.scheduling;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.fabric8.kubernetes.api.model.Quantity;

/**
 */
@JsonDeserialize(
        using = JsonDeserializer.None.class
)
public class PodGroupSpec implements KubernetesResource {
    private int minMember;
    private String queue;
    private String priorityClassName;
    private Map<String, Quantity> minResources = new HashMap<String, Quantity>();

    @Override
    public String toString() {
        return "PodGroupSpec{" +
                "minMember='" + minMember + '\'' +
                ", queue='" + queue + '\'' +
                ", priorityClassName='" + priorityClassName + '\'' +
                ", minResources='" + minResources + '\'' +
                '}';
    }

    public int getMinMember() {
        return minMember;
    }

    public void setMinMember(int minMember) {
        this.minMember = minMember;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getPriorityClassName() {
        return priorityClassName;
    }

    public void setPriorityClassName(String priorityClassName) {
        this.priorityClassName = priorityClassName;
    }

    public void addToMinResources(String resource, Quantity value) { minResources.put(resource, value); };

    public void addToMinResources(Map<String, Quantity> resources) { minResources.putAll(resources); };

    public void removeFromMinResources(String resource) { minResources.remove(resource); };

    public void removeFromMinResources(Map<String, Quantity> resources) {
        for (String resource: resources.keySet()) {
            minResources.remove(resource);
        }
    };

    public Map<String, Quantity> getMinResources() { return minResources; };

    public void setMinResources(Map<String, Quantity> resources) { minResources = resources; };

    public Boolean hasMinResources() { return minResources.size() > 0; };

}