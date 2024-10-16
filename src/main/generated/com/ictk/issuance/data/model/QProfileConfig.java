package com.ictk.issuance.data.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProfileConfig is a Querydsl query type for ProfileConfig
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProfileConfig extends EntityPathBase<ProfileConfig> {

    private static final long serialVersionUID = 48999813L;

    public static final QProfileConfig profileConfig = new QProfileConfig("profileConfig");

    public final StringPath comment = createString("comment");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath ctntData = createString("ctntData");

    public final StringPath dataHash = createString("dataHash");

    public final StringPath description = createString("description");

    public final StringPath profId = createString("profId");

    public final StringPath profName = createString("profName");

    public final StringPath profType = createString("profType");

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final StringPath version = createString("version");

    public QProfileConfig(String variable) {
        super(ProfileConfig.class, forVariable(variable));
    }

    public QProfileConfig(Path<? extends ProfileConfig> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProfileConfig(PathMetadata metadata) {
        super(ProfileConfig.class, metadata);
    }

}

