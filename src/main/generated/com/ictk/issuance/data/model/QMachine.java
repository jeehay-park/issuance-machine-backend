package com.ictk.issuance.data.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMachine is a Querydsl query type for Machine
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMachine extends EntityPathBase<Machine> {

    private static final long serialVersionUID = 1798311681L;

    public static final QMachine machine = new QMachine("machine");

    public final StringPath comment = createString("comment");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final ListPath<Device, QDevice> devices = this.<Device, QDevice>createList("devices", Device.class, QDevice.class, PathInits.DIRECT2);

    public final StringPath etc = createString("etc");

    public final StringPath mcnId = createString("mcnId");

    public final StringPath mcnName = createString("mcnName");

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QMachine(String variable) {
        super(Machine.class, forVariable(variable));
    }

    public QMachine(Path<? extends Machine> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMachine(PathMetadata metadata) {
        super(Machine.class, metadata);
    }

}

