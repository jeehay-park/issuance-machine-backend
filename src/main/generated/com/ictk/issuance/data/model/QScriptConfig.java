package com.ictk.issuance.data.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QScriptConfig is a Querydsl query type for ScriptConfig
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QScriptConfig extends EntityPathBase<ScriptConfig> {

    private static final long serialVersionUID = -896264845L;

    public static final QScriptConfig scriptConfig = new QScriptConfig("scriptConfig");

    public final StringPath comment = createString("comment");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath ctntData = createString("ctntData");

    public final StringPath dataHash = createString("dataHash");

    public final StringPath description = createString("description");

    public final StringPath scrtId = createString("scrtId");

    public final StringPath scrtName = createString("scrtName");

    public final StringPath scrtType = createString("scrtType");

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final StringPath version = createString("version");

    public QScriptConfig(String variable) {
        super(ScriptConfig.class, forVariable(variable));
    }

    public QScriptConfig(Path<? extends ScriptConfig> path) {
        super(path.getType(), path.getMetadata());
    }

    public QScriptConfig(PathMetadata metadata) {
        super(ScriptConfig.class, metadata);
    }

}

