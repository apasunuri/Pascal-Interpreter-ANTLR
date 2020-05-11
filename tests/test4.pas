program test4;

var
    a, b: real;

begin 
    a := 10;
    while a < 20 do
        begin
            writeln('value of a:');
            writeln(a);
            a := a + 1
        end;
    writeln();
    for b := 1 to 8 do
        begin
            writeln('SIN, COS, EXP, LN of b:');
            writeln(sin(b));
            writeln(cos(b));
            writeln(exp(b));
            writeln(ln(b));
            writeln()
        end 
end.

(*  Expected Output:
    value of a:
    10.0
    value of a:
    11.0
    value of a:
    12.0
    value of a:
    13.0
    value of a:
    14.0
    value of a:
    15.0
    value of a:
    16.0
    value of a:
    17.0
    value of a:
    18.0
    value of a:
    19.0

    SIN, COS, EXP, LN of b:
    0.8414709848078965
    0.5403023058681398
    2.718281828459045
    0.0

    SIN, COS, EXP, LN of b:
    0.9092974268256817
    -0.4161468365471424
    7.38905609893065
    0.6931471805599453

    SIN, COS, EXP, LN of b:
    0.1411200080598672
    -0.9899924966004454
    20.085536923187668
    1.0986122886681098

    SIN, COS, EXP, LN of b:
    -0.7568024953079282
    -0.6536436208636119
    54.598150033144236
    1.3862943611198906

    SIN, COS, EXP, LN of b:
    -0.9589242746631385
    0.28366218546322625
    148.4131591025766
    1.6094379124341003

    SIN, COS, EXP, LN of b:
    -0.27941549819892586
    0.960170286650366
    403.4287934927351
    1.791759469228055

    SIN, COS, EXP, LN of b:
    0.6569865987187891
    0.7539022543433046
    1096.6331584284585
    1.9459101490553132

    SIN, COS, EXP, LN of b:
    0.9893582466233818
    -0.14550003380861354
    2980.9579870417283
    2.0794415416798357
*)