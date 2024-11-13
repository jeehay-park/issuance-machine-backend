package com.ictk.issuance.data.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCodeEnum is a Querydsl query type for CodeEnum
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCodeEnum extends EntityPathBase<CodeEnum> {

    private static final long serialVersionUID = -770210540L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCodeEnum codeEnum = new QCodeEnum("codeEnum");

    public final QCodeInfo codeInfo;

    public final StringPath description = createString("description");

    public final StringPath enumId = createString("enumId");

    public final NumberPath<Long> enumSeq = createNumber("enumSeq", Long.class);

    public final StringPath enumValue = createString("enumValue");

    public final StringPath ip = createString("ip");

    public final BooleanPath isMandatory = createBoolean("isMandatory");

    public final NumberPath<Long> order = createNumber("order", Long.class);

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public QCodeEnum(String variable) {
        this(CodeEnum.class, forVariable(variable), INITS);
    }

    public QCodeEnum(Path<? extends CodeEnum> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCodeEnum(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCodeEnum(PathMetadata metadata, PathInits inits) {
        this(CodeEnum.class, metadata, inits);
    }

    public QCodeEnum(Class<? extends CodeEnum> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.codeInfo = inits.isInitialized("codeInfo") ? new QCodeInfo(forProperty("codeInfo")) : null;
    }

}

