# Arquitetura Pipes e Filtros

A arquitetura Pipes e Filtros funciona dividindo o processamento em etapas, onde cada uma tem uma função e passa o resultado para a próxima.

No HospitalCare, podemos usar como exemplo o agendamento de uma consulta. Primeiro o sistema recebe os dados, depois verifica se estão corretos, processa o agendamento e por último salva as informações.

O fluxo seria:

Entrada → Validação → Processamento → Persistência

A vantagem dessa arquitetura é que cada etapa pode ser tratada separadamente. Assim, se precisar mudar alguma validação, por exemplo, não seria necessário alterar todo o processamento.