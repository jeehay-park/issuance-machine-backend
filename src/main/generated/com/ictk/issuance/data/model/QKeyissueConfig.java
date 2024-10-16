package com.ictk.issuance.data.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QKeyissueConfig is a Querydsl query type for KeyissueConfig
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QKeyissueConfig extends EntityPathBase<KeyissueConfig> {

    private static final long serialVersionUID = -1299390014L;

    public static final QKeyissueConfig keyissueConfig = new QKeyissueConfig("keyissueConfig");

    public final StringPath comment = createString("comment");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath ctntData = createString("ctntData");

    public final StringPath dataHash = createString("dataHash");

    public final StringPath description = createString("description");

    public final StringPath keyisId = createString("keyisId");

    public final StringPath keyisName = createString("keyisName");

    public final StringPath keyisType = createString("keyisType");

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final StringPath version = createString("version");

    public QKeyissueConfig(String variable) {
        super(KeyissueConfig.class, forVariable(variable));
    }

    public QKeyissueConfig(Path<? extends KeyissueConfig> path) {
        super(path.getType(), path.getMetadata());
    }

    public QKeyissueConfig(PathMetadata metadata) {
        super(KeyissueConfig.class, metadata);
    }

}

