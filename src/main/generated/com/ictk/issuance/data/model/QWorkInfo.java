package com.ictk.issuance.data.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QWorkInfo is a Querydsl query type for WorkInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWorkInfo extends EntityPathBase<WorkInfo> {

    private static final long serialVersionUID = 132725733L;

    public static final QWorkInfo workInfo = new QWorkInfo("workInfo");

    public final NumberPath<Long> check_size = createNumber("check_size", Long.class);

    public final StringPath comment = createString("comment");

    public final NumberPath<Long> completed_size = createNumber("completed_size", Long.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath customer = createString("customer");

    public final StringPath detailMsg = createString("detailMsg");

    public final StringPath device_name = createString("device_name");

    public final DateTimePath<java.time.LocalDateTime> due_date = createDateTime("due_date", java.time.LocalDateTime.class);

    public final NumberPath<Long> failed_size = createNumber("failed_size", Long.class);

    public final StringPath is_lock = createString("is_lock");

    public final StringPath mcn_id = createString("mcn_id");

    public final StringPath order_no = createString("order_no");

    public final StringPath param = createString("param");

    public final StringPath paramExt = createString("paramExt");

    public final StringPath prog_id = createString("prog_id");

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final StringPath snr_id = createString("snr_id");

    public final DateTimePath<java.time.LocalDateTime> startedAt = createDateTime("startedAt", java.time.LocalDateTime.class);

    public final StringPath status = createString("status");

    public final StringPath tag_name = createString("tag_name");

    public final NumberPath<Long> target_size = createNumber("target_size", Long.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final StringPath work_id = createString("work_id");

    public final StringPath work_no = createString("work_no");

    public QWorkInfo(String variable) {
        super(WorkInfo.class, forVariable(variable));
    }

    public QWorkInfo(Path<? extends WorkInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWorkInfo(PathMetadata metadata) {
        super(WorkInfo.class, metadata);
    }

}

