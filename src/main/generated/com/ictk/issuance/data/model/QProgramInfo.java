package com.ictk.issuance.data.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProgramInfo is a Querydsl query type for ProgramInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProgramInfo extends EntityPathBase<ProgramInfo> {

    private static final long serialVersionUID = 1878913804L;

    public static final QProgramInfo programInfo = new QProgramInfo("programInfo");

    public final StringPath comment = createString("comment");

    public final StringPath companyCode = createString("companyCode");

    public final StringPath countryCode = createString("countryCode");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final StringPath etcOption = createString("etcOption");

    public final StringPath interfaceType = createString("interfaceType");

    public final BooleanPath isEncryptedSn = createBoolean("isEncryptedSn");

    public final StringPath keyisId = createString("keyisId");

    public final ListPath<KeyissueConfig, QKeyissueConfig> keyIssueInfo = this.<KeyissueConfig, QKeyissueConfig>createList("keyIssueInfo", KeyissueConfig.class, QKeyissueConfig.class, PathInits.DIRECT2);

    public final StringPath packageType = createString("packageType");

    public final StringPath param = createString("param");

    public final StringPath paramExt = createString("paramExt");

    public final StringPath product = createString("product");

    public final StringPath profId = createString("profId");

    public final ListPath<ProfileConfig, QProfileConfig> profileConfig = this.<ProfileConfig, QProfileConfig>createList("profileConfig", ProfileConfig.class, QProfileConfig.class, PathInits.DIRECT2);

    public final StringPath progId = createString("progId");

    public final StringPath progName = createString("progName");

    public final ListPath<ScriptConfig, QScriptConfig> scriptInfo = this.<ScriptConfig, QScriptConfig>createList("scriptInfo", ScriptConfig.class, QScriptConfig.class, PathInits.DIRECT2);

    public final StringPath scrtId = createString("scrtId");

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final StringPath sessionHandler = createString("sessionHandler");

    public final StringPath status = createString("status");

    public final StringPath testCode = createString("testCode");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QProgramInfo(String variable) {
        super(ProgramInfo.class, forVariable(variable));
    }

    public QProgramInfo(Path<? extends ProgramInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProgramInfo(PathMetadata metadata) {
        super(ProgramInfo.class, metadata);
    }

}

