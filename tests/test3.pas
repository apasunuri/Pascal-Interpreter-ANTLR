program test3;

var
    temp_f: real;
    temp_c: real;

begin
    writeln('Enter the Weather in Farenheit');
    readln(temp_f);
    temp_c := (5 * (temp_f - 32)) / 9;
    writeln('Temperature in Celcius');
    writeln(temp_c);
    if(temp_c <= 0) then
        writeln('It is below freezing temperature.')
    else
        writeln('It is above freezing temperature.')
end.

(*  Expected Output:
    Enter the Weather in Farenheit
    Inputted Temperature
    Temperatures in Celcius
    Inputted Temperature in Celcius
    It is below/above freezing temperature.
*)