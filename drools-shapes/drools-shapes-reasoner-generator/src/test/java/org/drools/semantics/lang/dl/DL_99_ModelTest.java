/*
 * Copyright 2011 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.semantics.lang.dl;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.semantics.builder.DLFactory;
import org.drools.semantics.builder.DLFactoryBuilder;
import org.drools.semantics.builder.model.*;
import org.drools.semantics.builder.model.compilers.ModelCompiler;
import org.drools.semantics.builder.model.compilers.ModelCompilerFactory;
import org.drools.semantics.util.SemanticWorkingSetConfigData;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.Assert.*;



@SuppressWarnings("restriction")
public class DL_99_ModelTest {



    protected DLFactory factory = DLFactoryBuilder.newDLFactoryInstance();

    @Rule
    public static TemporaryFolder folder;

    @BeforeClass
    public static void construct() {
        folder = new TemporaryFolder();
    }

    @AfterClass
    public static void destruct() {
        folder.delete();
    }


    @Test
    @Ignore // upgrade after refactor
    public void testDRLModelGenerationInternal() {
        String source = "kmr2" + File.separator + "kmr2_miniExample.manchester";
        Resource res = ResourceFactory.newClassPathResource( source );
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        StatefulKnowledgeSession kSession = kbase.newStatefulKnowledgeSession();

        OntoModel results = factory.buildModel( "kmr2mini", res, kSession );

        ModelCompiler compiler = ModelCompilerFactory.newModelCompiler( ModelFactory.CompileTarget.DRL );
        DRLModel drlModel = (DRLModel) compiler.compile( results );

        System.err.println( drlModel.getDRL() );


        ModelCompiler jcompiler = ModelCompilerFactory.newModelCompiler( ModelFactory.CompileTarget.JAR );
        JarModel jarModel = (JarModel) jcompiler.compile( results );

        assertTrue( jarModel.save( folder.getRoot().getAbsolutePath() ) );

        try {
            FileOutputStream fos = new FileOutputStream( folder.newFile( "test.jar" ) );
            byte[] content = jarModel.buildJar().toByteArray();

            fos.write( content, 0, content.length );
            fos.flush();
            fos.close();
        } catch ( IOException e ) {
            fail( e.getMessage() );
        }



        System.err.println( results );

    }



    @Test
    public void testDRLModelGenerationExternal() {
        String source = "kmr2" + File.separator + "kmr2_miniExample.manchester";
        Resource res = ResourceFactory.newClassPathResource( source );
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        StatefulKnowledgeSession kSession = kbase.newStatefulKnowledgeSession();

        factory.setInferenceStrategy( DLFactory.INFERENCE_STRATEGY.EXTERNAL );
        OntoModel results = factory.buildModel( "kmr2mini", res, kSession );

        ModelCompiler compiler = ModelCompilerFactory.newModelCompiler( ModelFactory.CompileTarget.DRL );
        DRLModel drlModel = (DRLModel) compiler.compile( results );

        System.err.println( drlModel.getDRL() );


        ModelCompiler jcompiler = ModelCompilerFactory.newModelCompiler( ModelFactory.CompileTarget.JAR );
        JarModel jarModel = (JarModel) jcompiler.compile( results );

        assertTrue( jarModel.save( folder.getRoot().getAbsolutePath() ) );

        try {
            FileOutputStream fos = new FileOutputStream( folder.newFile( "test.jar" ) );
            byte[] content = jarModel.buildJar().toByteArray();

            fos.write( content, 0, content.length );
            fos.flush();
            fos.close();
        } catch ( IOException e ) {
            fail( e.getMessage() );
        }



        System.err.println( results );

    }



    @Test
    @Ignore //visualization test
    public void testGraphModelGeneration() {
//        String source = "org/drools/semantics/lang/dl/kmr2_miniExample.manchester";
        String source = "kmr2" + File.separator + "KMR_OntologyLatest.manchester.owl";
        Resource res = ResourceFactory.newClassPathResource( source );
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        StatefulKnowledgeSession kSession = kbase.newStatefulKnowledgeSession();

        factory.setInferenceStrategy( DLFactory.INFERENCE_STRATEGY.EXTERNAL );
        OntoModel results = factory.buildModel( "kmr2", res, kSession );

        ModelCompiler compiler = ModelCompilerFactory.newModelCompiler( ModelFactory.CompileTarget.GRAPH );
        GraphModel gModel = (GraphModel) compiler.compile( results );

        gModel.display();

        System.err.println( gModel );


        try {
            while (true) {
                Thread.sleep(10000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }





    @Test
    public void testXSDModelGeneration() {
        String source = "kmr2" + File.separator + "kmr2_miniExample.manchester";
        Resource res = ResourceFactory.newClassPathResource( source );
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        StatefulKnowledgeSession kSession = kbase.newStatefulKnowledgeSession();

        factory.setInferenceStrategy( DLFactory.INFERENCE_STRATEGY.EXTERNAL );

        OntoModel results = factory.buildModel( "kmr2mini", res, kSession );


        ModelCompiler compiler = ModelCompilerFactory.newModelCompiler( ModelFactory.CompileTarget.XSD );
        XSDModel xsdModel = (XSDModel) compiler.compile( results );

        xsdModel.stream( System.out );

    }



    @Test
    public void testXSDExternalModelGeneration() {
        String source = "kmr2" + File.separator + "kmr2_miniExample.manchester";
        Resource res = ResourceFactory.newClassPathResource(source);

        OntoModel results = factory.buildModel( "kmr2mini", res );


        ModelCompiler compiler = ModelCompilerFactory.newModelCompiler( ModelFactory.CompileTarget.XSD );
        XSDModel xsdModel = (XSDModel) compiler.compile( results );

        xsdModel.stream( System.out );

    }





    @Test
    public void testWorkingSetModelGeneration() {
        String source = "kmr2" + File.separator + "kmr2_miniExample.manchester";
        Resource res = ResourceFactory.newClassPathResource( source );

        OntoModel results = factory.buildModel( "kmr2mini", res );


        ModelCompiler compiler = ModelCompilerFactory.newModelCompiler( ModelFactory.CompileTarget.WORKSET );
        WorkingSetModel wsModel = (WorkingSetModel) compiler.compile( results );

        SemanticWorkingSetConfigData ws = wsModel.getWorkingSet();

        System.out.println(ws);

    }


    @Test
    public void testFullKMR2XSDModelGeneration() {
        String source = "kmr2" + File.separator + "kmr2_miniExample.manchester";
//        String source = "kmr2/KMR_Ontology.ttl";
        Resource res = ResourceFactory.newClassPathResource( source );
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        StatefulKnowledgeSession kSession = kbase.newStatefulKnowledgeSession();

        factory.setInferenceStrategy( DLFactory.INFERENCE_STRATEGY.EXTERNAL );

        OntoModel results = factory.buildModel( "kmr2", res, kSession );


        ModelCompiler jcompiler =  ModelCompilerFactory.newModelCompiler( ModelFactory.CompileTarget.JAR );
        JarModel jarModel = (JarModel) jcompiler.compile( results );

        try {
            FileOutputStream fos = new FileOutputStream( folder.newFile( "kmr2.jar" ) );
            byte[] content = jarModel.buildJar().toByteArray();

            fos.write( content, 0, content.length );
            fos.flush();
            fos.close();
        } catch ( IOException e ) {
            fail( e.getMessage() );
        }


        /**************************************************************************************************************/


        ModelCompiler compiler = ModelCompilerFactory.newModelCompiler( ModelFactory.CompileTarget.XSDX );
        compiler.setMode(ModelCompiler.Mode.FLAT);
        SemanticXSDModel xsdModel = (SemanticXSDModel) compiler.compile( results );

        xsdModel.stream( System.out );
//        xsdModel.streamBindings( System.out );



        try {
            FileOutputStream fos = new FileOutputStream( folder.newFile( "kmr2.xsd" ) );
            xsdModel.stream( fos );
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            FileOutputStream fos = new FileOutputStream( folder.newFile("bindings.xjb") );
            xsdModel.streamBindings( fos );
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }













    @Test
    public void testConyardComplexModelGeneration() {

        Resource res = ResourceFactory.newClassPathResource( "conyard.ttl" );
        factory.setInferenceStrategy( DLFactory.INFERENCE_STRATEGY.EXTERNAL );
        OntoModel results = factory.buildModel( "conyard", res );

        results.flatten();

        ModelCompiler compiler = ModelCompilerFactory.newModelCompiler( ModelFactory.CompileTarget.XSDX );
        compiler.setMode(ModelCompiler.Mode.FLAT);
        SemanticXSDModel xsdModel = (SemanticXSDModel) compiler.compile( results );

        xsdModel.stream( System.out );
        //        xsdModel.streamBindings( System.out );


        String pack = "<http://org.drools.conyard.owl#>";
        Concept activity = results.getConcept(pack.replace("#", "#Activity") );
        Concept painting = results.getConcept(pack.replace("#", "#Painting") );
        Concept ironInst = results.getConcept(pack.replace("#", "#IronInstallation") );
        Concept wallRais = results.getConcept(pack.replace("#", "#WallRaising") );
        Concept inspectn = results.getConcept(pack.replace("#", "#Inspection") );

        assertNotNull( painting );
        assertNotNull( ironInst );
        assertNotNull( wallRais );


        Concept fallProt = results.getConcept(pack.replace("#", "#FallProtection") );
        Concept frostProt = results.getConcept(pack.replace("#", "#FrostProtection") );
        Concept fireProt = results.getConcept(pack.replace("#", "#FireProtection") );

        assertNotNull( fallProt );
        assertNotNull( fireProt );
        assertNotNull( frostProt );

        Concept beltX = results.getConcept(pack.replace("#", "#BeltBrandX") );
        Concept glove = results.getConcept(pack.replace("#", "#Gloves") );
        Concept gloveX = results.getConcept(pack.replace("#", "#GlovesBrandX") );

        assertNotNull( beltX );
        assertNotNull( gloveX );
        assertNotNull( glove );


        Concept fire = results.getConcept(pack.replace("#", "#Fire") );
        Concept anotherfire = results.getConcept(pack.replace("#", "#AnotherFire") );
        Concept anotherheat = results.getConcept(pack.replace("#", "#AnotherHeat") );
        Concept testfire = results.getConcept(pack.replace("#", "#TestFire") );

        assertNotNull( fire );
        assertNotNull( anotherfire );
        assertNotNull( anotherheat );
        assertNotNull( testfire );



        assertTrue( beltX.getSuperConcepts().contains( fallProt ) );
        assertTrue( gloveX.getSuperConcepts().contains( fireProt ) );
        assertTrue( gloveX.getSuperConcepts().contains( glove ) );
        assertTrue( glove.getSuperConcepts().contains( frostProt ) );

        assertTrue( testfire.getSuperConcepts().contains( fire ) );
        assertTrue( anotherfire.getSuperConcepts().contains( fire ) );
        assertTrue( anotherheat.getSuperConcepts().contains( anotherfire ) );


        assertTrue( checkProperty( painting, pack, "involvesPersonsAsActivity", "Person", 1, null, true) );
        assertTrue( checkProperty( painting, pack, "involvesLabourersAsPainting", "Labourer", 1, null, true ) );
        assertTrue( checkProperty( painting, pack, "requiresEquipmentsAsActivity", "Equipment", 0, null, true ) );
        assertTrue( checkProperty( painting, pack, "requiresPaintsAsPainting", "Paint", 1, null, true ) );
        assertTrue( checkProperty( painting, pack, "requiresStairAsPainting", "Stair", 1, 1, true ) );
        assertTrue( checkProperty( painting, pack, "requires", "Equipment", 0, null, false ) );
        assertTrue( checkProperty( painting, pack, "involves", "Person", 0, null, false ) );


        assertEquals( 17, ironInst.getProperties().size() );
        assertTrue( checkProperty( ironInst, pack, "involvesMasonsAsIronInstallation", "Mason", 1, null, true ) );
        assertTrue( checkProperty( ironInst, pack, "requiresWeldingTorchsAsIronInstallation", "WeldingTorch", 1, null, true ) );
        assertTrue( checkProperty( ironInst, pack, "requiresIronBarsAsIronInstallation", "IronBar", 1, null, true ) );
        assertTrue( checkProperty( ironInst, pack, "requiresGrindersAsIronInstallation", "Grinder", 1, null, true ) );
        assertTrue( checkProperty( ironInst, pack, "requiresCraneAsIronInstallation", "Crane", 1, 1, true ) );
        assertTrue( checkProperty( ironInst, pack, "involvesSmithsAsIronInstallation", "Smith", 1, 4, true) );
        assertTrue( checkProperty( ironInst, pack, "involvesPersonsAsActivity", "Person", 1, null, true ) );
        assertTrue( checkProperty( ironInst, pack, "involvesLabourersAsIronInstallation", "Labourer", 2, null, true ) );
        assertTrue( checkProperty( ironInst, pack, "requiresEquipmentsAsActivity", "Equipment", 0, null, true ) );
        assertTrue( checkProperty( ironInst, pack, "hasComment", "xsd:string", 0, null, false ) );
        assertTrue( checkProperty( ironInst, pack, "hasCommentStringAsActivity", "xsd:string", 1, 1, true ) );

        assertTrue( checkProperty( wallRais, pack, "involvesMasonsAsWallRaising", "Mason", 3, null, true ) );
        assertTrue( checkProperty( wallRais, pack, "requiresBricksAsWallRaising", "Bricks", 1, 1, true ) );
        assertFalse( checkProperty( wallRais, pack, "requiresBricksesAsWallRaising", "Bricks", 1, null, true ) );

        assertTrue( checkProperty( inspectn, pack, "involvesPersonsAsActivity", "Person", 1, null, true ) );
        assertTrue( checkProperty( inspectn, pack, "involvesPersonAsInspection", "Person", 1, 1, true ) );
        assertTrue( checkProperty( inspectn, pack, "requiresEquipmentsAsActivity", "Equipment", 0, null, true ) );
        assertTrue( checkProperty( inspectn, pack, "requiresEquipmentsAsInspection", "Equipment", 0, 3, true ) );

//
        for ( Concept con : results.getConcepts() ) {
            if ( con.getName().endsWith( "Range" ) || con.getName().endsWith( "Domain" ) || con.getName().endsWith( "Filler" ) ) {
                assertTrue( con.isAbstrakt() );
                assertTrue( con.isAnonymous() );
                assertFalse( con.isPrimitive() );
            }
        }
        
    }

    private boolean checkProperty( Concept base, String pack, String propName, String target, Integer minCard, Integer maxCard, boolean restricted ) {

        PropertyRelation rel = base.getProperties().get( pack.replace("#", "#"+propName) );
        if ( rel == null ) {
            return false;
        }
        if ( ! rel.getTarget().getName().equals( target ) ) {
            return false;
        }
        return rel.getMinCard() == minCard && rel.getMaxCard() == maxCard && rel.getName().equals( propName ) && rel.isRestricted() == restricted;
    }


}