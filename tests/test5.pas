program test5;

var
    a, b, c, sum: real;

begin
    a := 8;
    b := 6;
    sum := 0;
    while a < 16 do
        begin
            a := a + 1;
            if(a > 10) then 
                continue
            else
                writeln(a) 
        end;
    for c := 0 to b do
        begin
            if(c = 5) then
                break
            else
                sum := sum + c
        end;
    writeln();
    writeln('Total Sum:');
    writeln(sum)
end.

(*  Expected Output:
    9.0
    10.0

    Total Sum:
    10.0
*)