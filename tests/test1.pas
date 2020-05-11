program test1;

var
    x: real;
    y: real;

begin
    if(5 <> 7) then
        begin
            writeln(5 / 7);
            writeln('Valid Statement')
        end
    else
        writeln('Not a Valid Statement');
    if(5 > 7) then
        writeln(sqrt(5 / 7))
    else
        writeln('Not a Valid Statement')
end.

(*  Expected Output:
    0.7142857142857143
    Valid Statement
    Not a Valid Statement
*)