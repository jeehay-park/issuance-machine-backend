package com.ictk.issuance.data.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProfileConfig is a Querydsl query type for ProfileConfig
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProfileConfig extends EntityPathBase<ProfileConfig> {

    private static final long serialVersionUID = 48999813L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProfileConfig profileConfig = new QProfileConfig("profileConfig");

    public final StringPath comment = createString("comment");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath ctntData = createString("ctntData");

    public final StringPath dataHash = createString("dataHash");

    public final StringPath description = createString("description");

    public final StringPath profId = createString("profId");

    public final StringPath profName = createString("profName");

    public final StringPath profType = createString("profType");

    public final QProgramInfo programInfoProfileId;

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final StringPath version = createString("version");

    public QProfileConfig(String variable) {
        this(ProfileConfig.class, forVariable(variable), INITS);
    }

    public QProfileConfig(Path<? extends ProfileConfig> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProfileConfig(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProfileConfig(PathMetadata metadata, PathInits inits) {
        this(ProfileConfig.class, metadata, inits);
    }

    public QProfileConfig(Class<? extends ProfileConfig> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.programInfoProfileId = inits.isInitialized("programInfoProfileId") ? new QProgramInfo(forProperty("programInfoProfileId")) : null;
    }

}

