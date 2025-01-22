package com.ictk.issuance.data.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDeviceSessionInfo is a Querydsl query type for DeviceSessionInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDeviceSessionInfo extends EntityPathBase<DeviceSessionInfo> {

    private static final long serialVersionUID = 797287016L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDeviceSessionInfo deviceSessionInfo = new QDeviceSessionInfo("deviceSessionInfo");

    public final StringPath chipSn = createString("chipSn");

    public final StringPath comment = createString("comment");

    public final StringPath dvcId = createString("dvcId");

    public final StringPath error = createString("error");

    public final StringPath param = createString("param");

    public final StringPath paramExt = createString("paramExt");

    public final StringPath result = createString("result");

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final StringPath sessId = createString("sessId");

    public final DateTimePath<java.time.LocalDateTime> sessionDate = createDateTime("sessionDate", java.time.LocalDateTime.class);

    public final StringPath sessionNo = createString("sessionNo");

    public final StringPath status = createString("status");

    public final NumberPath<Integer> tkMsecTime = createNumber("tkMsecTime", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final StringPath workdetId = createString("workdetId");

    public final StringPath workId = createString("workId");

    public final QWorkInfo workInfo;

    public QDeviceSessionInfo(String variable) {
        this(DeviceSessionInfo.class, forVariable(variable), INITS);
    }

    public QDeviceSessionInfo(Path<? extends DeviceSessionInfo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDeviceSessionInfo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDeviceSessionInfo(PathMetadata metadata, PathInits inits) {
        this(DeviceSessionInfo.class, metadata, inits);
    }

    public QDeviceSessionInfo(Class<? extends DeviceSessionInfo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.workInfo = inits.isInitialized("workInfo") ? new QWorkInfo(forProperty("workInfo"), inits.get("workInfo")) : null;
    }

}

