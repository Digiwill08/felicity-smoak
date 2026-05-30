package com.arrow.acts.application.adapters.persistence.mongodb.documents;

import com.arrow.acts.domain.models.enums.IntelligenceConfidenceLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

/**
 * Documento MongoDB para persistir reportes de inteligencia.
 *
 * <p>Se almacena en la colección {@code intelligence_reports} de la base
 * de datos {@code acts_intelligence}. Elegida MongoDB para este agregado
 * por su naturaleza semi-estructurada, alta frecuencia de escritura y
 * fuentes de datos variables (ARGUS, cámaras, informantes, satélites).</p>
 */
@Document(collection = "intelligence_reports")
@Getter
@Setter
@NoArgsConstructor
public class IntelligenceReportDocument {

    @Id
    private String id;

    @Field("subject_id")
    private Long subjectId;

    @Field("source")
    private String source;

    @Field("observation")
    private String observation;

    @Field("location")
    private String location;

    @Field("estimated_concentration")
    private double estimatedConcentration;

    @Field("confidence_level")
    private IntelligenceConfidenceLevel confidenceLevel;

    @Field("reported_at")
    private Instant reportedAt;

    @Field("processed")
    private boolean processed;
}
