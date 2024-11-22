package com.ictk.issuance.data.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QScriptConfig is a Querydsl query type for ScriptConfig
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QScriptConfig extends EntityPathBase<ScriptConfig> {

    private static final long serialVersionUID = -896264845L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QScriptConfig scriptConfig = new QScriptConfig("scriptConfig");

    public final StringPath comment = createString("comment");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath ctntData = createString("ctntData");

    public final StringPath dataHash = createString("dataHash");

    public final StringPath description = createString("description");

    public final QProgramInfo programInfoScriptId;

    public final StringPath scrtId = createString("scrtId");

    public final StringPath scrtName = createString("scrtName");

    public final StringPath scrtType = createString("scrtType");

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final StringPath version = createString("version");

    public QScriptConfig(String variable) {
        this(ScriptConfig.class, forVariable(variable), INITS);
    }

    public QScriptConfig(Path<? extends ScriptConfig> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QScriptConfig(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QScriptConfig(PathMetadata metadata, PathInits inits) {
        this(ScriptConfig.class, metadata, inits);
    }

    public QScriptConfig(Class<? extends ScriptConfig> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.programInfoScriptId = inits.isInitialized("programInfoScriptId") ? new QProgramInfo(forProperty("programInfoScriptId")) : null;
    }

}

