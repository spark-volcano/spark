package sh.volcano.scheduling;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.fabric8.kubernetes.api.model.KubernetesResource;

/**
 */
@JsonDeserialize(
        using = JsonDeserializer.None.class
)
public class PodGroupStatus implements KubernetesResource {
    private String phase;
    // only phase now

    @Override
    public String toString() {
        return "PodGroupStatus{" +
                "phase='" + phase + '\'' +
                '}';
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }
}
