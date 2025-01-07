package com.ictk.issuance.data.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWorkHandler is a Querydsl query type for WorkHandler
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWorkHandler extends EntityPathBase<WorkHandler> {

    private static final long serialVersionUID = 1409884339L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWorkHandler workHandler = new QWorkHandler("workHandler");

    public final StringPath comment = createString("comment");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath detailMsg = createString("detailMsg");

    public final QDevice device;

    public final StringPath dvcId = createString("dvcId");

    public final StringPath hdlId = createString("hdlId");

    public final StringPath hdlName = createString("hdlName");

    public final StringPath mcnId = createString("mcnId");

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final StringPath status = createString("status");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final StringPath workId = createString("workId");

    public QWorkHandler(String variable) {
        this(WorkHandler.class, forVariable(variable), INITS);
    }

    public QWorkHandler(Path<? extends WorkHandler> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWorkHandler(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWorkHandler(PathMetadata metadata, PathInits inits) {
        this(WorkHandler.class, metadata, inits);
    }

    public QWorkHandler(Class<? extends WorkHandler> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.device = inits.isInitialized("device") ? new QDevice(forProperty("device"), inits.get("device")) : null;
    }

}

