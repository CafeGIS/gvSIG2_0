FasdUAS 1.101.10   ��   ��    k             l    	 ��  r     	  	  n      
  
 1    ��
�� 
psxp  l     ��  I    �� ��
�� .earsffdralis        afdr   f     ��  ��   	 o      ���� 0 thepath thePath��        l     �� ��    - 'set pathToMeTXT to (path to me) as text         l     ������  ��        l  
  ��  r   
     m   
    
 none     o      ���� 0 theerror theError��        l     �� ��     set oldVersion to "1.0.2"         l     �� ��    < 6set oldAndamiMD5 to "71fff1b38308db5323ccde794253e910"          l    !�� ! I   �� " #
�� .sysodlogaskr        TEXT " m     $ $ @ :Por favor, cierre gvSIG antes de continuar la instalaci�n.    # �� % &
�� 
disp % m    ��
�� stic    & �� '��
�� 
btns ' J     ( (  )�� ) m     * *  Ok   ��  ��  ��      + , + l   ) -�� - I   )���� .
�� .sysostdfalis    ��� null��   . �� / 0
�� 
prmp / l    1�� 1 m     2 2 " Por favor, seleccione gvSIG    ��   0 �� 3 4
�� 
ftyp 3 m     5 5 
 APPL    4 �� 6��
�� 
lfiv 6 m   " #��
�� boovfals��  ��   ,  7 8 7 l  * 1 9�� 9 r   * 1 : ; : 1   * -��
�� 
rslt ; o      ����  0 bundletoupdate bundleToUpdate��   8  < = < l     ������  ��   =  > ? > l  2 s @�� @ O   2 s A B A k   8 r C C  D E D r   8 C F G F l  8 ? H�� H c   8 ? I J I o   8 ;����  0 bundletoupdate bundleToUpdate J m   ; >��
�� 
ctxt��   G o      ���� ,0 pathtobundletoupdate pathToBundleToUpdate E  K�� K Q   D r L M N L k   G a O O  P Q P r   G _ R S R n   G [ T U T 4   V [�� V
�� 
cobj V m   Y Z����  U n   G V W X W 4   O V�� Y
�� 
cfol Y m   R U Z Z  Contents:Resources:gvSIG    X 4   G O�� [
�� 
cobj [ o   K N���� ,0 pathtobundletoupdate pathToBundleToUpdate S o      ���� $0 gvsigapplication gvSIGApplication Q  \�� \ l  ` `�� ]��   ] ` Zset gvSIGContents to folder "Contents:Resources:gvSIG:gvSIG 1.0.2 build 905.app:Contents:"   ��   M R      �� ^ _
�� .ascrerr ****      � **** ^ o      ���� 0 err   _ �� `��
�� 
errn ` o      ���� 0 nerr  ��   N k   i r a a  b c b r   i l d e d o   i j���� 0 err   e o      ���� 0 theerror theError c  f�� f r   m r g h g o   m n���� 0 nerr   h o      ����  0 theerrornumber theErrorNumber��  ��   B m   2 5 i i�null     ߀��  �
Finder.appU  0Z'wv����  <�:0�X8����Trwvp9�����brl�MACS   alis    h  	Manzanita                  ��(VH+    �
Finder.app                                                       F���        ����  	                CoreServices    ��F      ���      �  �  �  0Manzanita:System:Library:CoreServices:Finder.app   
 F i n d e r . a p p   	 M a n z a n i t a  &System/Library/CoreServices/Finder.app  / ��  ��   ?  j k j l  t � l�� l Z   t � m n���� m >  t y o p o o   t u���� 0 theerror theError p m   u x q q 
 none    n k   | � r r  s t s r   | � u v u m   |  w w  Salir    v o      ���� 0 but1   t  x y x r   � � z { z I  � ��� | }
�� .sysodlogaskr        TEXT | b   � � ~  ~ b   � � � � � m   � � � � I CEste gvSIG no parece ser la versi�n esperada por este actualizador.    � o   � ���
�� 
ret   o   � �����  0 theerrornumber theErrorNumber } �� � �
�� 
disp � m   � ���
�� stic     � �� ���
�� 
btns � J   � � � �  ��� � o   � ����� 0 but1  ��  ��   { o      ���� 0 dialogreply dialogReply y  � � � I  � �������
�� .aevtquitnull���    obj ��  ��   �  ��� � L   � �����  ��  ��  ��  ��   k  � � � l     �� ���   � � �set andamiMD5 to word -1 of (do shell script "cd \"" & POSIX path of (gvSIGContents as alias) & "\"Resources/Java/ && md5 andami.jar")    �  � � � l     �� ���   � K Eif ((andamiMD5 is not oldAndamiMD5) or (theError is not "none")) then    �  � � � l     �� ���   �  	set but1 to "Actualizar"    �  � � � l     �� ���   � " 	set but2 to "No actualizar"    �  � � � l     �� ���   � � �	set dialogReply to display dialog "Este gvSIG no parece ser la versi�n esperada por este actualizador." with icon caution buttons {but1, but2} default button 2    �  � � � l     �� ���   � 5 /	if button returned of dialogReply is but2 then    �  � � � l     �� ���   �  		quit    �  � � � l     �� ���   �  		return    �  � � � l     �� ���   �  	end if    �  � � � l     �� ���   �  end if    �  � � � l     ������  ��   �  � � � l     �� ���   � T Nsi llegamos aqui, es que MD5 es OK o usuario quiere actualizar de todas formas    �  � � � l     �� ���   � 0 *eliminar las librerias que no se necesitan    �  � � � l     �� ���   � � �set POSIXLibraryPath to POSIX path of alias ((gvSIGApplication as text) & --":Contents:Resources:Java:gvSIG:extensiones:com.iver.cit.gvsig:lib:")    �  � � � l     �� ���   � P Jdo shell script "rm -f \"" & POSIXLibraryPath & "\"jecwcompress-0.1.0.jar"    �  � � � l     �� ���   � I Cdo shell script "rm -f \"" & POSIXLibraryPath & "\"jgdal-0.4.0.jar"    �  � � � l     �� ���   � J Ddo shell script "rm -f \"" & POSIXLibraryPath & "\"jmrsid-0.1.0.jar"    �  � � � l     ������  ��   �  � � � l     �� ���   �  copiar el nuevo contenido    �  � � � l  � � ��� � r   � � � � � n   � � � � � 1   � ���
�� 
psxp � 4   � ��� �
�� 
alis � l  � � ��� � b   � � � � � l  � � ��� � c   � � � � � l  � � ��� � I  � ��� ���
�� .earsffdralis        afdr �  f   � ���  ��   � m   � ���
�� 
ctxt��   � m   � � � �   Contents:Resources:Updates   ��   � o      ���� "0 posixpathtoorig POSIXPathToOrig��   �  � � � l  � � ��� � r   � � � � � n   � � � � � 1   � ���
�� 
psxp � 4   � ��� �
�� 
alis � l  � � ��� � b   � � � � � l  � � ��� � c   � � � � � o   � ����� $0 gvsigapplication gvSIGApplication � m   � ���
�� 
ctxt��   � m   � � � �  
:Contents:   ��   � o      ���� "0 posixpathtodest POSIXPathToDest��   �  � � � l  � � ��� � I  � ��� ���
�� .sysoexecTEXT���     TEXT � b   � � � � � b   � � � � � b   � � � � � b   � � � � � m   � � � �  cp -rf -p "    � o   � ����� "0 posixpathtoorig POSIXPathToOrig � m   � � � � 
 "* "    � o   � ����� "0 posixpathtodest POSIXPathToDest � m   � � � �  "   ��  ��   �  � � � l     ������  ��   �  � � � l     �� ���   �  copiar la cache    �  � � � l     �� ���   � e _set POSIXPathCache to POSIX path of alias (((path to me) as text) & "Contents:Resources:Cache")    �  � � � l     �� ���   � 	 try    �  � � � l     � ��   � R L	do shell script "cp -R -p \"" & POSIXPathCache & "\"* ~/gvSIG/.data/cache/"    �  �  � l     �~�~    on error      l     �}�}   S M	do shell script "cd ~;mkdir gvSIG;cd gvSIG;mkdir .data;cd .data;mkdir cache"     l     �|�|   R L	do shell script "cp -R -p \"" & POSIXPathCache & "\"* ~/gvSIG/.data/cache/"    	 l     �{
�{  
  end try   	  l     �z�y�z  �y   �x l  ��w I  ��v
�v .sysodlogaskr        TEXT m   � � * $Instalaci�n del piloto 3D completada    �u
�u 
disp m   � ��t
�t stic    �s�r
�s 
btns J   � �q m   �   Ok   �q  �r  �w  �x       �p�p   �o
�o .aevtoappnull  �   � **** �n�m�l�k
�n .aevtoappnull  �   � **** k              +!!  7""  >##  j$$  �%%  �&&  �'' �j�j  �m  �l   �i�h�i 0 err  �h 0 nerr   2�g�f�e �d $�c�b�a *�`�_�^ 2�] 5�\�[�Z�Y�X i�W�V�U�T Z�S�R(�Q q w�P ��O�N�M�L�K ��J ��I � � ��H
�g .earsffdralis        afdr
�f 
psxp�e 0 thepath thePath�d 0 theerror theError
�c 
disp
�b stic   
�a 
btns�` 
�_ .sysodlogaskr        TEXT
�^ 
prmp
�] 
ftyp
�\ 
lfiv�[ 
�Z .sysostdfalis    ��� null
�Y 
rslt�X  0 bundletoupdate bundleToUpdate
�W 
ctxt�V ,0 pathtobundletoupdate pathToBundleToUpdate
�U 
cobj
�T 
cfol�S $0 gvsigapplication gvSIGApplication�R 0 err  ( �G�F�E
�G 
errn�F 0 nerr  �E  �Q  0 theerrornumber theErrorNumber�P 0 but1  
�O 
ret 
�N stic    �M 0 dialogreply dialogReply
�L .aevtquitnull���    obj 
�K 
alis�J "0 posixpathtoorig POSIXPathToOrig�I "0 posixpathtodest POSIXPathToDest
�H .sysoexecTEXT���     TEXT�k)j  �,E�O�E�O�����kv� O*����a fa  O_ E` Oa  <_ a &E` O *a _ /a a /a k/E` OPW X  �E�O�E` UO�a  3a  E` !Oa "_ #%_ %�a $�_ !kv� E` %O*j &OhY hO*a ')j  a &a (%/�,E` )O*a '_ a &a *%/�,E` +Oa ,_ )%a -%_ +%a .%j /Oa 0���a 1kv� ascr  ��ޭ