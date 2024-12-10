package com.ictk.issuance.data.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWorkInfo is a Querydsl query type for WorkInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWorkInfo extends EntityPathBase<WorkInfo> {

    private static final long serialVersionUID = 132725733L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWorkInfo workInfo = new QWorkInfo("workInfo");

    public final NumberPath<Long> checkSize = createNumber("checkSize", Long.class);

    public final StringPath comment = createString("comment");

    public final NumberPath<Long> completedSize = createNumber("completedSize", Long.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath customer = createString("customer");

    public final StringPath description = createString("description");

    public final StringPath detailMsg = createString("detailMsg");

    public final StringPath deviceName = createString("deviceName");

    public final StringPath dueDate = createString("dueDate");

    public final NumberPath<Long> failedSize = createNumber("failedSize", Long.class);

    public final BooleanPath isLock = createBoolean("isLock");

    public final QMachine machineInfo;

    public final StringPath mcnId = createString("mcnId");

    public final StringPath orderNo = createString("orderNo");

    public final StringPath param = createString("param");

    public final StringPath paramExt = createString("paramExt");

    public final StringPath progId = createString("progId");

    public final QProgramInfo programInfo;

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final StringPath snrId = createString("snrId");

    public final QSNRule snRuleInfo;

    public final DateTimePath<java.time.LocalDateTime> startedAt = createDateTime("startedAt", java.time.LocalDateTime.class);

    public final StringPath status = createString("status");

    public final StringPath tagName = createString("tagName");

    public final NumberPath<Long> targetSize = createNumber("targetSize", Long.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final StringPath workId = createString("workId");

    public final StringPath workNo = createString("workNo");

    public QWorkInfo(String variable) {
        this(WorkInfo.class, forVariable(variable), INITS);
    }

    public QWorkInfo(Path<? extends WorkInfo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWorkInfo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWorkInfo(PathMetadata metadata, PathInits inits) {
        this(WorkInfo.class, metadata, inits);
    }

    public QWorkInfo(Class<? extends WorkInfo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.machineInfo = inits.isInitialized("machineInfo") ? new QMachine(forProperty("machineInfo")) : null;
        this.programInfo = inits.isInitialized("programInfo") ? new QProgramInfo(forProperty("programInfo")) : null;
        this.snRuleInfo = inits.isInitialized("snRuleInfo") ? new QSNRule(forProperty("snRuleInfo")) : null;
    }

}

