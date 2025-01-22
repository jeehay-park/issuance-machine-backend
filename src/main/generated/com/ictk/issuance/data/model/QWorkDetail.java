package com.ictk.issuance.data.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWorkDetail is a Querydsl query type for WorkDetail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWorkDetail extends EntityPathBase<WorkDetail> {

    private static final long serialVersionUID = -1450639928L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWorkDetail workDetail = new QWorkDetail("workDetail");

    public final StringPath comment = createString("comment");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath detailData = createString("detailData");

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final StringPath workdetId = createString("workdetId");

    public final StringPath workId = createString("workId");

    public final QWorkInfo workInfo;

    public QWorkDetail(String variable) {
        this(WorkDetail.class, forVariable(variable), INITS);
    }

    public QWorkDetail(Path<? extends WorkDetail> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWorkDetail(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWorkDetail(PathMetadata metadata, PathInits inits) {
        this(WorkDetail.class, metadata, inits);
    }

    public QWorkDetail(Class<? extends WorkDetail> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.workInfo = inits.isInitialized("workInfo") ? new QWorkInfo(forProperty("workInfo"), inits.get("workInfo")) : null;
    }

}

