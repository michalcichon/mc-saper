<?php
include("nagl.php");
echo "<H2> Tablice wielowymiarowe</H2><p>";
//Tablica wielowymiarowa
$dane[0][0]=10;
$dane[0][1]=20;
$dane[0][2]=30;
$dane[1][0]=40;
$dane[1][1]=50;
$dane[1][2]=60;
$dane[2][0]=100;
$dane[2][1]=200;
$dane[2][2]=300;
for($wiersz=0; $wiersz<3; $wiersz++ ){
for($kolumna=0;kolumna<3;$kolumna++)
printf("%6d", $dane[$wiersz][$kolumna]);
echo "<br>";
}
echo "<hr/>";
echo"wypisanie przez echo -" . $dane . "<br/>";
print"wypisanie przez print -" . $dane . "<br/>";
print"wypisanie przez print_t - <br/>";
include("stopka.php");
?>