package com.ictk.issuance.data.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCodeInfo is a Querydsl query type for CodeInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCodeInfo extends EntityPathBase<CodeInfo> {

    private static final long serialVersionUID = -770091839L;

    public static final QCodeInfo codeInfo = new QCodeInfo("codeInfo");

    public final StringPath codeGroup = createString("codeGroup");

    public final StringPath codeId = createString("codeId");

    public final StringPath codeName = createString("codeName");

    public final StringPath comment = createString("comment");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final StringPath status = createString("status");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QCodeInfo(String variable) {
        super(CodeInfo.class, forVariable(variable));
    }

    public QCodeInfo(Path<? extends CodeInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCodeInfo(PathMetadata metadata) {
        super(CodeInfo.class, metadata);
    }

}

