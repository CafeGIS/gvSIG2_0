echo  CLEANING BASE PROJECTS  
echo  CLEANING BASE PROJECTS  
echo  CLEANING BASE PROJECTS  
cd ../build/projects/gvsig-plugin-base
mvn clean
echo CLEANING  3D  PROJECTS
echo CLEANING  3D  PROJECTS  
echo CLEANING  3D  PROJECTS
cd ../gvsig-plugin-3D
mvn clean
echo  CLEANING ANIMATION PROJECTS  
echo  CLEANING ANIMATION PROJECTS  
echo  CLEANING ANIMATION PROJECTS  
cd ../gvsig-plugin-animation
mvn clean
mvn install -Dmaven.test.skip -Dinstall-extension
echo  CLEANING GENERATING INSTALLER  
echo  CLEANING GENERATING INSTALLER  
echo  CLEANING GENERATING INSTALLER  
cd ../../../ext3Dgui
mvn install -Dmaven.test.skip -Dinstall-extension
mvn install -Dmaven.test.skip -Dgenerate-install
