
    #include <AR/gsub_es.h>
    #include <Eden/glm.h>
    #include <jni.h>
    #include <ARWrapper/ARToolKitWrapperExportedAPI.h>
    #include <unistd.h> // chdir()
    #include <android/log.h>

    // Utility preprocessor directive so only one change needed if Java class name changes
    #define JNIFUNCTION_DEMO(sig) Java_com_immersionslabs_lcatalog_augment_ARNativeRenderer_##sig

        extern "C" {
            JNIEXPORT void JNICALL JNIFUNCTION_DEMO(demoInitialise(JNIEnv * env, jobject object)) ;
            JNIEXPORT void JNICALL JNIFUNCTION_DEMO(demoShutdown(JNIEnv * env, jobject object)) ;
            JNIEXPORT void JNICALL JNIFUNCTION_DEMO(demoSurfaceCreated(JNIEnv * env, jobject object)) ;
            JNIEXPORT void JNICALL JNIFUNCTION_DEMO(demoSurfaceChanged(JNIEnv * env, jobject object, jint w, jint h)) ;
            JNIEXPORT void JNICALL JNIFUNCTION_DEMO(demoDrawFrame(JNIEnv * env, jobject obj)) ;
        };

        typedef struct ARModel {
            int patternID;
            ARdouble transformationMatrix[16];
            bool visible;
            GLMmodel *obj;
        } ARModel;

        #define NUM_MODELS 34
        static ARModel models[NUM_MODELS] = {0};

        static float lightAmbient[4] = {0.1f, 0.1f, 0.1f, 1.0f};
        static float lightDiffuse[4] = {1.0f, 1.0f, 1.0f, 1.0f};
        static float lightPosition[4] = {1.0f, 1.0f, 1.0f, 0.0f};
        static float lightSpecular[4] = {1.0f, 1.0f, 1.0f, 1.0f};

        static float materialShininess[] = {50.0};
        static float materialSpecular[] = {1.0f, 1.0f, 1.0f, 1.0f};
        static float materialEmission[] = {0.3f, 0.2f, 0.2f, 0.0f};

        JNIEXPORT void JNICALL JNIFUNCTION_DEMO(demoInitialise(JNIEnv * env, jobject object)) {

            const char *model0file = "/storage/emulated/0/L_CATALOG/cache/Data/models/bedsofa.obj";
            const char *model1file = "/storage/emulated/0/L_CATALOG/cache/Data/models/dressing_table.obj";
            const char *model2file = "/storage/emulated/0/L_CATALOG/cache/Data/models/outdoorsofa.obj";
            const char *model3file = "/storage/emulated/0/L_CATALOG/cache/Data/models/wardrobe.obj";
            const char *model4file = "/storage/emulated/0/L_CATALOG/cache/Data/models/interior2.obj";
            const char *model5file = "/storage/emulated/0/L_CATALOG/cache/Data/models/parasona.obj";
            const char *model6file = "/storage/emulated/0/L_CATALOG/cache/Data/models/barrelset.obj";
            const char *model7file = "/storage/emulated/0/L_CATALOG/cache/Data/models/teakbed.obj";
            const char *model8file = "/storage/emulated/0/L_CATALOG/cache/Data/models/wallpaint.obj";
            const char *model9file = "/storage/emulated/0/L_CATALOG/cache/Data/models/florence_compact.obj";
            const char *model10file = "/storage/emulated/0/L_CATALOG/cache/Data/models/4seated_dinning_table.obj";
            const char *model11file = "/storage/emulated/0/L_CATALOG/cache/Data/models/alba_sheeshamcoffee_table.obj";
            const char *model12file = "/storage/emulated/0/L_CATALOG/cache/Data/models/chelsea.obj";
            const char *model13file = "/storage/emulated/0/L_CATALOG/cache/Data/models/Multiple_Frames_Buddha_Art_Wall_Painting.obj";
            const char *model14file = "/storage/emulated/0/L_CATALOG/cache/Data/models/floorlamp.obj";
            const char *model15file = "/storage/emulated/0/L_CATALOG/cache/Data/models/wallpartition.obj";
            const char *model16file = "/storage/emulated/0/L_CATALOG/cache/Data/models/ottomanchair.obj";
            const char *model17file = "/storage/emulated/0/L_CATALOG/cache/Data/models/kitchenunit.obj";
            const char *model18file = "/storage/emulated/0/L_CATALOG/cache/Data/models/Audi_R8.obj";
            const char *model19file = "/storage/emulated/0/L_CATALOG/cache/Data/models/royaloka_tvset.obj";
            const char *model20file = "/storage/emulated/0/L_CATALOG/cache/Data/models/norland.obj";
            const char *model21file = "/storage/emulated/0/L_CATALOG/cache/Data/models/window.obj";
            const char *model22file = "/storage/emulated/0/L_CATALOG/cache/Data/models/execuofc.obj";
            const char *model23file = "/storage/emulated/0/L_CATALOG/cache/Data/models/exewait.obj";
            const char *model24file = "/storage/emulated/0/L_CATALOG/cache/Data/models/emppod.obj";
            const char *model25file = "/storage/emulated/0/L_CATALOG/cache/Data/models/meetingtable.obj";
            const char *model26file = "/storage/emulated/0/L_CATALOG/cache/Data/models/officeparta.obj";
            const char *model27file = "/storage/emulated/0/L_CATALOG/cache/Data/models/officepartb.obj";
            const char *model28file = "/storage/emulated/0/L_CATALOG/cache/Data/models/expall.obj";
            const char *model29file = "/storage/emulated/0/L_CATALOG/cache/Data/models/empoda.obj";
            const char *model30file = "/storage/emulated/0/L_CATALOG/cache/Data/models/empodb.obj";
            const char *model31file = "/storage/emulated/0/L_CATALOG/cache/Data/models/entsfa.obj";
            const char *model32file = "/storage/emulated/0/L_CATALOG/cache/Data/models/Ferrari_Modena_Spider.obj";
            const char *model33file = "/storage/emulated/0/L_CATALOG/cache/Data/models/Porsche.obj";

            //Mapping to pattern 1 - bed sofa.obj
            models[0].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOG/cache/Data/patterns/pattern1.patt;80");
            arwSetMarkerOptionBool(models[0].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[0].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[0].obj = glmReadOBJ2(model0file, 0, 0); // context 0, don't read textures yet.
                if (!models[0].obj) {
                    LOGE("Error loading model from file '%s'.", model0file);
                    exit(-1);
                }
            glmScale(models[0].obj, 15.0f);
            //glmRotate(models[0].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[0].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE);
            models[0].visible = false;

            //Mapping to pattern 2 - dressing_table.obj
            models[1].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOG/cache/Data/patterns/pattern2.patt;80");
            arwSetMarkerOptionBool(models[1].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[1].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[1].obj = glmReadOBJ2(model1file, 0, 0); // context 1, don't read textures yet.
                if (!models[1].obj) {
                    LOGE("Error loading model from file '%s'.", model1file);
                    exit(-1);
                }
            glmScale(models[1].obj, 15.0f);
            //glmRotate(models[1].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[1].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE);
            models[1].visible = false;

            //Mapping to pattern 3 - outdoor sofa.obj
            models[2].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOG/cache/Data/patterns/pattern3.patt;80");
            arwSetMarkerOptionBool(models[2].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[2].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[2].obj = glmReadOBJ2(model2file, 0, 0); // context 2, don't read textures yet.
                    if (!models[2].obj) {
                      LOGE("Error loading model from file '%s'.", model2file);
                      exit(-1);
                    }
            glmScale(models[2].obj, 15.0f);
            //glmRotate(models[2].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[2].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
            models[2].visible = false;

            //Mapping to pattern 4 - wardrobe.obj
            models[3].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOG/cache/Data/patterns/pattern4.patt;80");
            arwSetMarkerOptionBool(models[3].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[3].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[3].obj = glmReadOBJ2(model3file, 0, 0); // context 3, don't read textures yet.
                    if (!models[3].obj) {
                      LOGE("Error loading model from file '%s'.", model3file);
                      exit(-1);
                    }
            glmScale(models[3].obj, 15.0f);
            //glmRotate(models[3].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[3].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
            models[3].visible = false;

            //Mapping to pattern 5 - study_table.obj
            models[4].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOG/cache/Data/patterns/pattern5.patt;80");
            arwSetMarkerOptionBool(models[4].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[4].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[4].obj = glmReadOBJ2(model4file, 0, 0); // context 4, don't read textures yet.
                    if (!models[4].obj) {
                      LOGE("Error loading model from file '%s'.", model4file);
                      exit(-1);
                    }
            glmScale(models[4].obj, 15.0f);
            //glmRotate(models[4].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[4].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
                        models[4].visible = false;

            //Mapping to pattern 6 - parasona.obj
            models[5].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOG/cache/Data/patterns/pattern6.patt;80");
            arwSetMarkerOptionBool(models[5].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[5].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[5].obj = glmReadOBJ2(model5file, 0, 0); // context 5, don't read textures yet.
                   if (!models[5].obj) {
                     LOGE("Error loading model from file '%s'.", model5file);
                     exit(-1);
                   }
            glmScale(models[5].obj, 15.0f);
            //glmRotate(models[5].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[5].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
                       models[5].visible = false;

            //Mapping to pattern 7 - Barrel Set.obj
            models[6].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOG/cache/Data/patterns/pattern7.patt;80");
            arwSetMarkerOptionBool(models[6].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[6].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[6].obj = glmReadOBJ2(model6file, 0, 0); // context 6, don't read textures yet.
                  if (!models[6].obj) {
                    LOGE("Error loading model from file '%s'.", model6file);
                    exit(-1);
                  }
            glmScale(models[6].obj, 15.0f);
            //glmRotate(models[6].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[6].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
                      models[6].visible = false;

            //Mapping to pattern 8 - teak bed.obj
            models[7].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOG/cache/Data/patterns/pattern8.patt;80");
            arwSetMarkerOptionBool(models[7].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[7].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[7].obj = glmReadOBJ2(model7file, 0, 0); // context 7, don't read textures yet.
                if (!models[7].obj) {
                  LOGE("Error loading model from file '%s'.", model7file);
                  exit(-1);
                }
            glmScale(models[7].obj, 15.0f);
            //glmRotate(models[7].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[7].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
                    models[7].visible = false;

            //Mapping to pattern 9 - wall paint.obj
            models[8].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOG/cache/Data/patterns/pattern9.patt;80");
            arwSetMarkerOptionBool(models[8].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[8].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[8].obj = glmReadOBJ2(model8file, 0, 0); // context 8, don't read textures yet.
                if (!models[8].obj) {
                  LOGE("Error loading model from file '%s'.", model8file);
                  exit(-1);
                }
            glmScale(models[8].obj, 15.0f);
            //glmRotate(models[8].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[8].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
                    models[8].visible = false;

            //Mapping to pattern 10 - florence compact sofa.obj
            models[9].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOG/cache/Data/patterns/pattern10.patt;80");
            arwSetMarkerOptionBool(models[9].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[9].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[9].obj = glmReadOBJ2(model9file, 0, 0); // context 9, don't read textures yet.
                if (!models[9].obj) {
                  LOGE("Error loading model from file '%s'.", model9file);
                  exit(-1);
                }
            glmScale(models[9].obj, 15.0f);
            //glmRotate(models[9].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[9].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
                    models[9].visible = false;

            //Mapping to pattern 11  - 4 Seated Dining table.obj
            models[10].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOG/cache/Data/patterns/pattern11.patt;80");
            arwSetMarkerOptionBool(models[10].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[10].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[10].obj = glmReadOBJ2(model10file, 0, 0); // context 10, don't read textures yet.
                if (!models[10].obj) {
                  LOGE("Error loading model from file '%s'.", model10file);
                  exit(-1);
                }
            glmScale(models[10].obj, 15.0f);
            //glmRotate(models[10].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[10].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
                    models[10].visible = false;

            //Mapping to pattern 12  - Alba Sheesham Coffee Table.obj
            models[11].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOG/cache/Data/patterns/pattern12.patt;80");
            arwSetMarkerOptionBool(models[11].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[11].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[11].obj = glmReadOBJ2(model11file, 0, 0); // context 11, don't read textures yet.
                if (!models[11].obj) {
                  LOGE("Error loading model from file '%s'.", model11file);
                  exit(-1);
                }
            glmScale(models[11].obj, 5.0f);
            //glmRotate(models[11].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[11].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
                    models[11].visible = false;

            //Mapping to pattern 13  - chelsea.obj
            models[12].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOG/cache/Data/patterns/pattern13.patt;80");
            arwSetMarkerOptionBool(models[12].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[12].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[12].obj = glmReadOBJ2(model12file, 0, 0); // context 12, don't read textures yet.
                if (!models[12].obj) {
                  LOGE("Error loading model from file '%s'.", model12file);
                  exit(-1);
                }
            glmScale(models[12].obj, 15.0f);
            //glmRotate(models[12].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[12].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
                    models[12].visible = false;

            //Mapping to pattern 14  - Multiple_Frames_Buddha_Art_Wall_Painting.obj
            models[13].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOG/cache/Data/patterns/pattern14.patt;80");
            arwSetMarkerOptionBool(models[13].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[13].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[13].obj = glmReadOBJ2(model13file, 0, 0); // context 13, don't read textures yet.
                if (!models[13].obj) {
                  LOGE("Error loading model from file '%s'.", model13file);
                  exit(-1);
                }
            glmScale(models[13].obj, 15.0f);
            //glmRotate(models[13].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[13].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
                    models[13].visible = false;

            //Mapping to pattern 15  - Floor Lamp.obj
            models[14].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOG/cache/Data/patterns/pattern15.patt;80");
            arwSetMarkerOptionBool(models[14].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[14].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[14].obj = glmReadOBJ2(model14file, 0, 0); // context 14, don't read textures yet.
                if (!models[14].obj) {
                  LOGE("Error loading model from file '%s'.", model14file);
                  exit(-1);
                }
            glmScale(models[14].obj, 15.0f);
            //glmRotate(models[14].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[14].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
                    models[14].visible = false;

            //Mapping to pattern 16  - wallpartition.obj
            models[15].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOG/cache/Data/patterns/pattern16.patt;80");
            arwSetMarkerOptionBool(models[15].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[15].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[15].obj = glmReadOBJ2(model15file, 0, 0); // context 15, don't read textures yet.
                if (!models[15].obj) {
                  LOGE("Error loading model from file '%s'.", model15file);
                  exit(-1);
                }
            glmScale(models[15].obj, 15.0f);
            //glmRotate(models[15].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[15].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
                    models[15].visible = false;

            //Mapping to pattern 17  - otootman.obj
            models[16].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOG/cache/Data/patterns/pattern17.patt;80");
            arwSetMarkerOptionBool(models[16].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[16].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[16].obj = glmReadOBJ2(model16file, 0, 0); // context 16, don't read textures yet.
                if (!models[16].obj) {
                  LOGE("Error loading model from file '%s'.", model16file);
                  exit(-1);
                }
            glmScale(models[16].obj, 15.0f);
            //glmRotate(models[16].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[16].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
                    models[16].visible = false;

            //Mapping to pattern 18  - kitchenunit.obj
            models[17].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOG/cache/Data/patterns/pattern18.patt;80");
            arwSetMarkerOptionBool(models[17].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[17].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[17].obj = glmReadOBJ2(model17file, 0, 0); // context 17, don't read textures yet.
                if (!models[17].obj) {
                  LOGE("Error loading model from file '%s'.", model17file);
                  exit(-1);
                }
            glmScale(models[17].obj, 15.0f);
            //glmRotate(models[17].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[17].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
                    models[17].visible = false;

            //Mapping to pattern 19  - goldchain.obj
            models[18].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOG/cache/Data/patterns/pattern19.patt;80");
            arwSetMarkerOptionBool(models[18].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[18].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[18].obj = glmReadOBJ2(model18file, 0, 0); // context 18, don't read textures yet.
                if (!models[18].obj) {
                  LOGE("Error loading model from file '%s'.", model18file);
                  exit(-1);
                }

            glmScale(models[18].obj, 2.0f);
            //glmRotate(models[18].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[18].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
                    models[18].visible = false;

            //Mapping to pattern 20  - royaloka_tvset.obj
            models[19].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOG/cache/Data/patterns/pattern20.patt;80");
            arwSetMarkerOptionBool(models[19].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[19].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[19].obj = glmReadOBJ2(model19file, 0, 0); // context 19, don't read textures yet.
                if (!models[19].obj) {
                  LOGE("Error loading model from file '%s'.", model19file);
                  exit(-1);
                }
            glmScale(models[19].obj, 15.0f);
            //glmRotate(models[19].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[19].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
                    models[19].visible = false;

            //Mapping to pattern 21  - norland.obj
            models[20].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOG/cache/Data/patterns/pattern21.patt;80");
            arwSetMarkerOptionBool(models[20].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[20].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[20].obj = glmReadOBJ2(model20file, 0, 0); // context 20, don't read textures yet.
                if (!models[20].obj) {
                  LOGE("Error loading model from file '%s'.", model20file);
                  exit(-1);
                }
            glmScale(models[20].obj, 15.0f);
            //glmRotate(models[20].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[20].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
                    models[20].visible = false;

            //Mapping to pattern 22  - window.obj
            models[21].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOG/cache/Data/patterns/pattern22.patt;80");
            arwSetMarkerOptionBool(models[21].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[21].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[21].obj = glmReadOBJ2(model21file, 0, 0); // context 21, don't read textures yet.
                if (!models[21].obj) {
                  LOGE("Error loading model from file '%s'.", model21file);
                  exit(-1);
                }
            glmScale(models[21].obj, 50.0f);
            //glmRotate(models[21].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[21].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
                    models[21].visible = false;

            //Mapping to pattern 23  - execuofc.obj
            models[22].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOG/cache/Data/patterns/pattern23.patt;80");
            arwSetMarkerOptionBool(models[22].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[22].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[22].obj = glmReadOBJ2(model22file, 0, 0); // context 23, don't read textures yet.
                if (!models[22].obj) {
                  LOGE("Error loading model from file '%s'.", model22file);
                  exit(-1);
                }
            glmScale(models[22].obj, 50.0f);
            //glmRotate(models[22].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[22].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
                    models[22].visible = false;

            //Mapping to pattern 24  - exewait.obj
            models[23].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOG/cache/Data/patterns/pattern24.patt;80");
            arwSetMarkerOptionBool(models[23].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[23].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[23].obj = glmReadOBJ2(model23file, 0, 0); // context 24, don't read textures yet.
                if (!models[23].obj) {
                  LOGE("Error loading model from file '%s'.", model23file);
                  exit(-1);
                }
            glmScale(models[23].obj, 50.0f);
            //glmRotate(models[23].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[23].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
                    models[23].visible = false;

            //Mapping to pattern 25  - empod.obj
            models[24].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOG/cache/Data/patterns/pattern25.patt;80");
            arwSetMarkerOptionBool(models[24].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[24].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[24].obj = glmReadOBJ2(model24file, 0, 0); // context 25, don't read textures yet.
                if (!models[24].obj) {
                  LOGE("Error loading model from file '%s'.", model24file);
                  exit(-1);
                }
            glmScale(models[24].obj, 50.0f);
            //glmRotate(models[24].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[24].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
                    models[24].visible = false;

            //Mapping to pattern 26  - meetingtable.obj
            models[25].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOG/cache/Data/patterns/pattern26.patt;80");
            arwSetMarkerOptionBool(models[25].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[25].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[25].obj = glmReadOBJ2(model25file, 0, 0); // context 26, don't read textures yet.
                if (!models[25].obj) {
                  LOGE("Error loading model from file '%s'.", model25file);
                  exit(-1);
                }
            glmScale(models[25].obj, 50.0f);
            //glmRotate(models[25].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[25].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
                    models[25].visible = false;

           //Mapping to pattern 27  - officeenvprta.obj
            models[26].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOG/cache/Data/patterns/pattern27.patt;80");
            arwSetMarkerOptionBool(models[26].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[26].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[26].obj = glmReadOBJ2(model26file, 0, 0); // context 27, don't read textures yet.
                if (!models[26].obj) {
                  LOGE("Error loading model from file '%s'.", model26file);
                  exit(-1);
                }
            glmScale(models[26].obj, 25.0f);
            //glmRotate(models[26].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[26].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
                    models[26].visible = false;

            //Mapping to pattern 28  - officeenvprtb.obj
            models[27].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOG/cache/Data/patterns/pattern28.patt;80");
            arwSetMarkerOptionBool(models[27].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[27].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[27].obj = glmReadOBJ2(model27file, 0, 0); // context 28, don't read textures yet.
                if (!models[27].obj) {
                  LOGE("Error loading model from file '%s'.", model27file);
                  exit(-1);
                }
            glmScale(models[27].obj, 25.0f);
            //glmRotate(models[27].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[27].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
                    models[27].visible = false;

            //Mapping to pattern 29  - expall.obj
            models[28].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOG/cache/Data/patterns/pattern29.patt;80");
            arwSetMarkerOptionBool(models[28].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[28].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[28].obj = glmReadOBJ2(model28file, 0, 0); // context 29, don't read textures yet.
                if (!models[28].obj) {
                  LOGE("Error loading model from file '%s'.", model28file);
                  exit(-1);
                }
            glmScale(models[28].obj, 20.0f);
            //glmRotate(models[28].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[28].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
                    models[28].visible = false;

            //Mapping to pattern 30  - empoda.obj
            models[29].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOG/cache/Data/patterns/pattern30.patt;80");
            arwSetMarkerOptionBool(models[29].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[29].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[29].obj = glmReadOBJ2(model29file, 0, 0); // context 30, don't read textures yet.
                if (!models[29].obj) {
                  LOGE("Error loading model from file '%s'.", model29file);
                  exit(-1);
                }
            glmScale(models[29].obj, 50.0f);
            //glmRotate(models[29].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[29].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
                    models[29].visible = false;

            //Mapping to pattern 31  - empodb.obj
            models[30].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOG/cache/Data/patterns/pattern31.patt;80");
            arwSetMarkerOptionBool(models[30].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[30].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[30].obj = glmReadOBJ2(model30file, 0, 0); // context 31, don't read textures yet.
                if (!models[30].obj) {
                  LOGE("Error loading model from file '%s'.", model30file);
                  exit(-1);
                }
            glmScale(models[30].obj, 50.0f);
            //glmRotate(models[30].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[30].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
                    models[30].visible = false;

            //Mapping to pattern 32  - entsfa.obj
            models[31].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOG/cache/Data/patterns/pattern32.patt;80");
            arwSetMarkerOptionBool(models[31].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[31].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[31].obj = glmReadOBJ2(model31file, 0, 0); // context 32, don't read textures yet.
                if (!models[31].obj) {
                  LOGE("Error loading model from file '%s'.", model31file);
                  exit(-1);
                }
            glmScale(models[31].obj, 50.0f);
            //glmRotate(models[31].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[31].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
                    models[31].visible = false;

            //Mapping to pattern 33  - Ferrari car.obj
            models[32].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOG/cache/Data/patterns/pattern33.patt;80");
            arwSetMarkerOptionBool(models[32].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[32].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[32].obj = glmReadOBJ2(model32file, 0, 0); // context 33, don't read textures yet.
                if (!models[32].obj) {
                  LOGE("Error loading model from file '%s'.", model32file);
                  exit(-1);
                }
            glmScale(models[32].obj, 10.0f);
            //glmRotate(models[32].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[32].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
                    models[32].visible = false;

            //Mapping to pattern 34  - Porsche car.obj
            models[33].patternID = arwAddMarker("single;/storage/emulated/0/L_CATALOG/cache/Data/patterns/pattern34.patt;80");
            arwSetMarkerOptionBool(models[33].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[33].patternID, ARW_MARKER_OPTION_FILTERED, true);

            models[33].obj = glmReadOBJ2(model33file, 0, 0); // context 34, don't read textures yet.
                if (!models[33].obj) {
                  LOGE("Error loading model from file '%s'.", model33file);
                  exit(-1);
                }
            glmScale(models[32].obj, 10.0f);
            //glmRotate(models[33].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[33].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE );
                    models[33].visible = false;

         }

    JNIEXPORT void JNICALL JNIFUNCTION_DEMO(demoShutdown(JNIEnv * env, jobject object)) {}

    JNIEXPORT void JNICALL JNIFUNCTION_DEMO(demoSurfaceCreated(JNIEnv * env, jobject object)) {
         glStateCacheFlush(); // Make sure we don't hold outdated OpenGL state.
         for (int i = 0;i < NUM_MODELS; i++) {
             if (models[i].obj) {
                 glmDelete(models[i].obj, 0);
                 models[i].obj = NULL;
             }
         }
     }

    JNIEXPORT void JNICALL JNIFUNCTION_DEMO(demoSurfaceChanged(JNIEnv * env, jobject object, jint w, jint h)) {
        // glViewport(0, 0, w, h) has already been set.
    }

    JNIEXPORT void JNICALL JNIFUNCTION_DEMO(demoDrawFrame(JNIEnv * env, jobject obj)) {

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // Set the projection matrix to that provided by ARToolKit.
        float proj[16];
        arwGetProjectionMatrix(proj);
        glMatrixMode(GL_PROJECTION);
        glLoadMatrixf(proj);
        glMatrixMode(GL_MODELVIEW);

        glStateCacheEnableDepthTest();

        glStateCacheEnableLighting();

        glEnable(GL_LIGHT0);

        for (int i = 0;i < NUM_MODELS; i++) {
            models[i].visible = arwQueryMarkerTransformation(models[i].patternID, models[i].transformationMatrix);

            if (models[i].visible) {
                glLoadMatrixf(models[i].transformationMatrix);

                glLightfv(GL_LIGHT0, GL_POSITION, lightPosition);

                glLightfv(GL_LIGHT0, GL_AMBIENT, lightAmbient);
                glLightfv(GL_LIGHT0, GL_DIFFUSE, lightDiffuse);
                glLightfv(GL_LIGHT0, GL_SPECULAR, lightSpecular);

                glLightModelfv(GL_LIGHT_MODEL_AMBIENT, lightAmbient);

                glMaterialfv(GL_FRONT, GL_SPECULAR, materialSpecular);
                glMaterialfv(GL_FRONT, GL_SHININESS, materialShininess);
                glMaterialfv(GL_FRONT, GL_EMISSION, materialEmission);

                glmDrawArrays(models[i].obj, 0);
            }
        }
    }