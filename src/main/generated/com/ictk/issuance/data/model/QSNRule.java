package com.ictk.issuance.data.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSNRule is a Querydsl query type for SNRule
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSNRule extends EntityPathBase<SNRule> {

    private static final long serialVersionUID = 1320122845L;

    public static final QSNRule sNRule = new QSNRule("sNRule");

    public final StringPath comment = createString("comment");

    public final NumberPath<Long> countSum = createNumber("countSum", Long.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> lastBurnDate = createDateTime("lastBurnDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> location = createNumber("location", Integer.class);

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final StringPath snrId = createString("snrId");

    public final StringPath snrName = createString("snrName");

    public final StringPath testCode = createString("testCode");

    public final NumberPath<Long> todayCount = createNumber("todayCount", Long.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QSNRule(String variable) {
        super(SNRule.class, forVariable(variable));
    }

    public QSNRule(Path<? extends SNRule> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSNRule(PathMetadata metadata) {
        super(SNRule.class, metadata);
    }

}

