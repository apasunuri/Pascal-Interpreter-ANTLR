program test9;

var
   a, b, c, min: real;

procedure findMin(x, y, z, m: real);
begin
    if x < y then
        m := x
   else
        m := y;
   
   if z < m then
        m := z
   else
        m := m
end;  

begin
    min := 0;
    writeln('Enter three numbers:');
    readln(a);
    readln(b);
    readln(c);
    findMin(a, b, c, min); 
    writeln('Minimum:');
    writeln(min)
end.

(*  Expected Output:
    Minimum:
    Minimum of values inputted
*)