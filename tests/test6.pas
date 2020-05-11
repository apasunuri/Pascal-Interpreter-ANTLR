program test6;

var 
    a, b: real;

begin
    var
        b: real;
    for var a := 0 to 9 do
    begin
        b := sqrt(a);
        case(b) of
            1 : writeln('a is a perfect square');
            2 : writeln('a is a perfect square');
            3 : writeln('a is a perfect square');
        end  
    end;
    a := 4;
    b := 6;
    writeln();
    writeln(a);
    writeln(b) 
end.

(*  Expected Output:
    a is a perfect square
    a is a perfect square
    a is a perfect square

    4.0
    6.0
*)