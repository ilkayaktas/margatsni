/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.ilkayaktas.margatsni.utils;

/**
 * Created by iaktas on 24/04/17.
 */

public final class AppConstants {

    public static final String SHARED_PREF_DBDREATED = "DatabaseCreated";
    
    
    public static final String STATUS_CODE_FAILED = "failed";

    public static final int API_STATUS_CODE_LOCAL_ERROR = 0;

    public static final String DB_NAME = "mindorks_mvp.db";
    public static final String PREF_NAME = "mindorks_pref";

    public static final long NULL_INDEX = -1L;

    public static final String SEED_DATABASE_OPTIONS = "seed/options.json";
    public static final String SEED_DATABASE_QUESTIONS = "seed/questions.json";

    public static final String TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss";

    public static final int REQ_CODE_SPEECH_INPUT = 5000;
    
    public static final int REPORT_RECORD_HISTORY_COUNT = 10;

    public static final String INSTAGRAM_AUTH_URL = "https://api.instagram.com/oauth/authorize/?";

    public static final String INSTAGRAM_ACCESS_TOKEN_URL = "https://api.instagram.com/oauth/access_token";

    public static final String INSTAGRAM_API_BASE_URL = "https://api.instagram.com/v1/";

    public static final String INSTAGRAM_CLIENT_ID = "82d5b817e82e41c39352649e29fd864e";

    public static final String INSTAGRAM_CLIENT_SECRET = "d570e68a21f544298256a2fb804739b1";

    public static final String INSTAGRAM_CALBACK_URL = "http://www.ilkayaktas.com";

    public static final String FIVEHUNDREDPX_CUSTOMER_KEY = "RI5BBGQqLPWkVckVYPbUKSr53Rd0yEQ4lFmLEu1e";

    public static final String FIVEHUNDREDPX_CUSTOMER_SECRET = "kMkLTphXzai5TvDf6LG5pSTRwlxLgE40IjMv9kZb";

    public static final String FIVEHUNDREDPX_CALBACK_URL = "http://www.ilkayaktas.com";

    public static final String FIVEHUNDREDPX_API_BASE_URL = "https://api.500px.com/v1/";

    private AppConstants() {
        // This utility class is not publicly instantiable
    }
}
