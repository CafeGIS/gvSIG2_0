<?xml version='1.0' encoding='ISO-8859-1' ?>
<!DOCTYPE helpset
  PUBLIC "-//Sun Microsystems Inc.//DTD JavaHelp HelpSet Version 2.0//EN"
         "../dtd/helpset_2_0.dtd">

<helpset version="1.0">

  <!-- title -->
  <title>gvSIG - Ayuda en linea</title>

  <!-- maps -->
  <maps>
     <homeID>top</homeID>
     <mapref location="map.jhm"/>
  </maps>

  <!-- views -->
  <view>
    <name>TOC</name>
    <label>Tabla de contenidos</label>
    <type>javax.help.TOCView</type>
    <data>toc.xml</data>
  </view>

  <view>
    <name>Index</name>
    <label>Indice analitico</label>
    <type>javax.help.IndexView</type>
    <data>index.xml</data>
  </view>

  <view>
    <name>Search</name>
    <label>Search</label>
    <type>javax.help.SearchView</type>
    <data engine="com.sun.java.help.search.DefaultSearchEngine">
      JavaHelpSearch
    </data>
  </view>

  <view>
    <name>Favorites</name>
    <label>Favoritos</label>
    <type>javax.help.FavoritesView</type>
  </view>

  <presentation default="true" displayviewimages="false">
     <name>main window</name>
     <size width="700" height="400" />
     <location x="200" y="200" />
     <title>gvSIG - Ayuda en linea</title>
     <image>toplevelfolder</image>
     <toolbar>
	<helpaction image="action.back">javax.help.BackAction</helpaction>
	<helpaction image="action.forward">javax.help.ForwardAction</helpaction>
	<helpaction>javax.help.SeparatorAction</helpaction>
	<helpaction image="action.home">javax.help.HomeAction</helpaction>
	<helpaction image="action.reload">javax.help.ReloadAction</helpaction>
	<helpaction>javax.help.SeparatorAction</helpaction>
	<helpaction image="action.print">javax.help.PrintAction</helpaction>
	<!--helpaction>javax.help.PrintSetupAction</helpaction-->
     </toolbar>
  </presentation>
</helpset>
