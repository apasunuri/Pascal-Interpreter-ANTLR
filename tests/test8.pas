program test8;

var
    b, g: real;

function power(num1, num2: real): real;
var
    result: real;
begin
    result := 1;
    for var g := 0 to num2 do
        result := result * num1;
    power := result
end;

begin
    g := 8;
    var
        d, e: real;
    for var a := 0 to g do
        begin
            case(a) of 
                4 : begin
                    writeln(sqrt(4.5));
                    writeln('Real Number')
                end;
                1.25 : begin
                    writeln(sqrt(1.25));
                    writeln('Another Real Number')
                end;
                3.14 : writeln('PI');
                2 : writeln('Another Real Number');
            end
        end;  
    b := power(2, 3);
    writeln(b);
    writeln(cos(b))
end.

(*  Expected Output:
    Another Real Number
    2.1213203435596424
    Real Number
    16.0
    -0.9576594803233847
*)